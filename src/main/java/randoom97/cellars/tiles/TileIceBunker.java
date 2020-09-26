package randoom97.cellars.tiles;

import org.apache.logging.log4j.Level;

import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import randoom97.cellars.Cellars;
import randoom97.cellars.ModBlocks;
import randoom97.cellars.ModItems;
import randoom97.cellars.config.CellarsConfig;
import randoom97.cellars.utils.CellarCore;

public class TileIceBunker extends CellarCore implements ITickable {

	public static final int SIZE = 4;

	private BlockPos door1, door2;
	private boolean doorsFound = true;
	private int[] size = new int[5];
	private boolean formed = false;
	private boolean doorsOpen = true;

	private int timeToNextTick = 0;
	private float internalTemp = 20;
	private long tickCount = 0;
	private boolean cooling = false;
	private float insulation =1;

	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block == Blocks.ICE || block == BlocksTFC.SEA_ICE || block == Blocks.SNOW || stack.getItem() == ModItems.iceChunks) {
				return true;
			}
			return false;
		}

		@Override
		protected void onContentsChanged(int slot) {
			TileIceBunker.this.markDirty();
		}
	};

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		if (compound.hasKey("internalTemp"))
			internalTemp = compound.getFloat("internalTemp");
		if (compound.hasKey("timeToNextTick"))
			timeToNextTick = compound.getInteger("timeToNextTick");
		if (compound.hasKey("cooling"))
			cooling = compound.getBoolean("cooling");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		compound.setFloat("internalTemp", internalTemp);
		compound.setInteger("timeToNextTick", timeToNextTick);
		compound.setBoolean("cooling", cooling);
		return compound;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
		}
		return super.getCapability(capability, facing);
	}

	public void setDoorsOpen(boolean open) {
		this.doorsOpen = open;
	}

	public boolean getDoorsOpen() {
		return doorsOpen;
	}

	public void setDoorsFound(boolean found) {
		this.doorsFound = true;
	}

	public boolean getDoorsFound() {
		return doorsFound;
	}

	public void setFormed(boolean formed) {
		this.formed = formed;
	}

	public boolean getFormed() {
		return formed;
	}

	public void setTemperature(float temp) {
		this.internalTemp = temp;
	}

	public float getTemperature() {
		return internalTemp;
	}

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}

		if (tickCount % 1200 == 0) {
			formed = isStructureComplete();
			Cellars.logger.log(Level.DEBUG, "Formation check " + (formed ? "passed" : "failed"));
		}
		tickCount++;
		if (tickCount % CellarsConfig.ICE_BUNKER_TICK_RATE == 0) {
			float environTemp = ClimateTFC.getActualTemp(pos);
			if (formed) {

				// check for door open
				doorsOpen = true;
				if (world.getBlockState(door1).getBlock() == ModBlocks.cellarDoor
						&& world.getBlockState(door2).getBlock() == ModBlocks.cellarDoor) {
					doorsOpen = world.getBlockState(door1).getValue(BlockDoor.OPEN)
							&& world.getBlockState(door2).getValue(BlockDoor.OPEN);
					Cellars.logger.log(Level.DEBUG, "Doors are " + (doorsOpen ? "open" : "closed"));
				} else {
					formed = false;
				}

				// environmental change
				float envTempChange = 0;
				if (doorsOpen) {
					envTempChange = (internalTemp - environTemp) * CellarsConfig.RATE_OF_CHANGE;
				} else {
					envTempChange = (internalTemp - environTemp)
							* (CellarsConfig.RATE_OF_CHANGE / insulation);
				}

				// cooling change
				float coolingChange = 0;
				if (cooling) {
					coolingChange = internalTemp * CellarsConfig.COOlING_ITEM_RATE;
				}

				internalTemp -= coolingChange;
				internalTemp -= envTempChange;

				
				for (int y = 1; y <= size[4]; y++) {
					for (int z = -size[2]; z <= size[0]; z++) {
						for (int x = -size[1]; x <= size[3]; x++) {
							updateContainer(pos.add(x, y, z), internalTemp);
						}
					}
				}
			} else {
				internalTemp = environTemp;
			}

		}
		if (timeToNextTick > 0) {
			timeToNextTick--;
			return;
		}

		boolean snowConsumed = false;

		// consume an item if there is one
		cooling = false;
		if (internalTemp > 0) {
			for (int i = itemStackHandler.getSlots() - 1; i >= 0; i--) {
				ItemStack stack = itemStackHandler.getStackInSlot(i);
				Block block = Block.getBlockFromItem(stack.getItem());
				if (block == Blocks.ICE || block == BlocksTFC.SEA_ICE || stack.getItem() == ModItems.iceChunks) {
					cooling = true;
					stack.shrink(1);
					markDirty();
					break;
				} else if (Block.getBlockFromItem(stack.getItem()) == Blocks.SNOW) {
					cooling = true;
					snowConsumed = true;
					stack.shrink(1);
					markDirty();
					break;
				}
			}
		}

		// update the tick timer
		if (internalTemp < 0) {
			timeToNextTick = CellarsConfig.FROZEN_TICK;
		} else if (internalTemp > 20F) {
			timeToNextTick = CellarsConfig.ROOM_TEMP_TICK;
		} else {
			float multiplier = 1F - (internalTemp / 20F);
			timeToNextTick = (int) ((CellarsConfig.FROZEN_TICK - CellarsConfig.ROOM_TEMP_TICK) * multiplier
					+ CellarsConfig.ROOM_TEMP_TICK);
		}

		if (snowConsumed) {
			timeToNextTick /= 2;
		}
	}

	private void updateContainer(BlockPos pos, float temp) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileCellarShelf) {
			((TileCellarShelf) te).updateShelf(temp);
		}
	}

	private boolean isStructureComplete() {
		door1 = null;
		door2 = null;
		formed = false;
		int[] CellarInfo = this.structureComplete();
		for (int i = 0; i < CellarInfo.length; i++) {
			// Cellars.logger.info(i + "" + CellarInfo[i]);
		}
		for (int i = 0; i < size.length; i++) {
			size[i] = CellarInfo[1 + i];
		}
		// Cellars.logger.info("cellar form check returned condition "+CellarInfo[0]);
		switch (CellarInfo[0]) {
		case 0:
			insulation = 1;
			break;
		case 1:
			insulation = CellarsConfig.BASICINSULATION;
			break;
		case 2:
			insulation = CellarsConfig.INDUSTRIALINSULATION;
			break;
		}
		formed = (CellarInfo[0] > 0);
		if (CellarInfo[0] > 0) {
			BlockPos[] doors = this.FindDoors(size);
			door1 = doors[0];
			// Cellars.logger.info(doors[0]);
			// Cellars.logger.info(doors[1]);
			door2 = doors[1];
			if (door1 == null || door2 == null) {
				formed = false;
				return false;
			}
			return (CellarInfo[0] > 0);
		}
		return false;
	}

}
