package randoom97.cellars.tiles;

import org.apache.logging.log4j.Level;

import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import randoom97.cellars.Cellars;
import randoom97.cellars.ModBlocks;
import randoom97.cellars.ModItems;
import randoom97.cellars.config.CellarsConfig;

public class TileIceBunker extends TileEntity implements ITickable {

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
							* (CellarsConfig.RATE_OF_CHANGE / CellarsConfig.INSULATION);
				}

				// cooling change
				float coolingChange = 0;
				if (cooling) {
					coolingChange = internalTemp * CellarsConfig.RATE_OF_CHANGE;
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

		// get size
		for (int direction = 0; direction < 4; direction++) {
			for (int dist = 1; dist < 6; dist++) {
				if (dist == 5) {
					Cellars.logger.log(Level.DEBUG, "Cellar too big!");
					return false;
				}
				Block block = null;
				switch (direction) {
				case 0:
					block = world.getBlockState(pos.add(0, 1, dist)).getBlock();
					break;
				case 1:
					block = world.getBlockState(pos.add(-dist, 1, 0)).getBlock();
					break;
				case 2:
					block = world.getBlockState(pos.add(0, 1, -dist)).getBlock();
					break;
				case 3:
					block = world.getBlockState(pos.add(dist, 1, 0)).getBlock();
					break;
				}

				if (block == ModBlocks.cellarBlock || block == ModBlocks.cellarDoor) {
					size[direction] = dist - 1;
					break;
				}
			}
		}
		for (int dist = 2; dist <= 5; dist++) {
			if (dist == 5) {
				Cellars.logger.log(Level.DEBUG, "Cellar too tall");
				return false;
			}
			Block block = world.getBlockState(pos.add(0, dist, 0)).getBlock();
			if (block == ModBlocks.cellarBlock) {
				size[4] = dist - 1;
				break;
			}
		}

		// check blocks and set entrance
		for (int y = 0; y <= size[4] + 1; y++) {
			for (int x = -size[1] - 1; x <= size[3] + 1; x++) {
				for (int z = -size[2] - 1; z <= size[0] + 1; z++) {

					// ice bunker
					if (y == 0 && x == 0 && z == 0)
						continue;

					Block block = world.getBlockState(pos.add(x, y, z)).getBlock();

					// skip the inside of the cellar
					if (y > 0 && y < size[4] + 1 && x > -size[1] - 1 && x < size[3] + 1 && z > -size[2] - 1
							&& z < size[0] + 1) {
						if (block == ModBlocks.cellarBlock) {
							Cellars.logger.log(Level.DEBUG, "Inside can't contain cellar block!");
							return false;
						}
						continue;
					}

					// corners
					if ((x == -size[1] - 1 || x == size[3] + 1) && (z == -size[2] - 1 || z == size[0] + 1)) {
						if (block == ModBlocks.cellarBlock) {
							continue;
						}
						Cellars.logger.log(Level.DEBUG, "Corners invalid at " + pos.add(x, y, z));
						return false;
					}

					// door
					if (y == 1 && block == ModBlocks.cellarDoor) {
						// door1 not null means another door was already found
						if (door1 != null) {
							Cellars.logger.log(Level.DEBUG, "Too many doors!");
							return false;
						}

						door1 = pos.add(x, y, z);
						if (x == -size[1] - 1) {
							door2 = door1.add(-1, 0, 0);
						}
						if (x == size[3] + 1) {
							door2 = door1.add(1, 0, 0);
						}
						if (z == -size[2] - 1) {
							door2 = door1.add(0, 0, -1);
						}
						if (z == size[2] + 1) {
							door2 = door1.add(0, 0, 1);
						}
						continue;
					}

					// upper part of door
					if (y == 2 && block == ModBlocks.cellarDoor && door1!= null ) {
						if (!pos.add(x, y, z).equals(door1.add(0, 1, 0))) {
							Cellars.logger.log(Level.DEBUG, "Upper door doesn't match lower door!");
							return false;
						}
						continue;
					}

					// wall
					if (!(block == ModBlocks.cellarBlock)) {
						Cellars.logger.log(Level.DEBUG, "Cellar wall incorrect at " + pos.add(x, y, z));
						return false;
					}
				}
			}
		}

		// no door found
		if (door1 == null) {
			Cellars.logger.log(Level.DEBUG, "No door was found!");
			return false;
		}

		// check the entrance
		Vec3i direction = door1.subtract(door2);
		boolean xAllign = direction.getX() != 0;
		for (int y = -1; y < 3; y++) {
			if (xAllign) {
				for (int z = -1; z <= 1; z++) {
					IBlockState state = world.getBlockState(door2.add(0, y, z));
					Block block = state.getBlock();
					if (z == 0 && (y == 0 || y == 1)) {
						if (!(block == ModBlocks.cellarDoor)) {
							Cellars.logger.log(Level.DEBUG, "Expected door2 but didn't get one");
							return false;
						}
						continue;
					} else if (!(block == ModBlocks.cellarBlock)) {
						Cellars.logger.log(Level.DEBUG, "Edges of door wrong at " + door2.add(0, y, z));
						return false;
					}
				}
			} else {
				for (int x = -1; x <= 1; x++) {
					Block block = world.getBlockState(door2.add(x, y, 0)).getBlock();
					if (x == 0 && (y == 0 || y == 1)) {
						if (!(block == ModBlocks.cellarDoor)) {
							Cellars.logger.log(Level.DEBUG, "Expected door2 but didn't get one");
							return false;
						}
					} else if (!(block == ModBlocks.cellarBlock)) {
						Cellars.logger.log(Level.DEBUG, "Edges of door wrong at " + door2.add(x, y, 0));
						return false;
					}
				}
			}
		}

		// door directions
		if (xAllign) {
			EnumFacing facing1 = world.getBlockState(door1).getValue(BlockDoor.FACING);
			if (facing1 != EnumFacing.EAST && facing1 != EnumFacing.WEST) {
				Cellars.logger.log(Level.DEBUG, "Door1 facing "+facing1);
				return false;
			}
			EnumFacing facing2 = world.getBlockState(door2).getValue(BlockDoor.FACING);
			if (facing2 != EnumFacing.EAST && facing2 != EnumFacing.WEST) {
				Cellars.logger.log(Level.DEBUG, "Door2 facing "+facing2);
				return false;
			}
		} else {
			EnumFacing facing1 = world.getBlockState(door1).getValue(BlockDoor.FACING);
			if (facing1 != EnumFacing.NORTH && facing1 != EnumFacing.SOUTH) {
				Cellars.logger.log(Level.DEBUG, "Door1 facing " + facing1);
				return false;
			}
			EnumFacing facing2 = world.getBlockState(door2).getValue(BlockDoor.FACING);
			if (facing2 != EnumFacing.NORTH && facing2 != EnumFacing.SOUTH) {
				Cellars.logger.log(Level.DEBUG, "Door2 facing "+facing2);
				return false;
			}
		}

		return true;
	}

}
