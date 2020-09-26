package randoom97.cellars.tiles;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import randoom97.cellars.config.CellarsConfig;
import randoom97.cellars.registry.CellarFoodTrait;

public class TileCellarShelf extends TileEntity implements ITickable {

	public static final int SIZE = 14;
	
	private float internalTemp = 20;
	private long ticks = 0;
	private int timeToDisconnect = CellarsConfig.ICE_BUNKER_TICK_RATE+10;
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
			if(cap != null) {
				return true;
			}
			return false;
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			TileCellarShelf.this.markDirty();
		}
	};

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		if(compound.hasKey("internalTemp"))
			internalTemp = compound.getFloat("internalTemp");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		compound.setFloat("internalTemp", internalTemp);
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

	public void updateShelf(float temp) {
		timeToDisconnect = CellarsConfig.ICE_BUNKER_TICK_RATE+10;
		internalTemp = temp;
	}
	
	public void setTimeToDisconnect(int time) {
		timeToDisconnect = time;
	}
	
	public int getTimeToDisconnect() {
		return timeToDisconnect;
	}
	
	public void setInternalTemp(float temp) {
		this.internalTemp = temp;
	}
	
	public void setTemperature(float temp) {
		this.internalTemp = temp;
	}
	
	public float getTemperature() {
		return internalTemp;
	}
	
	public void beforeBlockBreak() {
		for(int i = 0; i < itemStackHandler.getSlots(); i++) {
			IFood cap = itemStackHandler.getStackInSlot(i).getCapability(CapabilityFood.CAPABILITY, null);
			if(cap != null) {
				CapabilityFood.removeTrait(cap, CellarFoodTrait.COLD);
        		CapabilityFood.removeTrait(cap, CellarFoodTrait.FROZEN);
			}
		}
	}
	
	@Override
	public void update() {
		if(world.isRemote) {
			return;
		}
		if(timeToDisconnect >= 0) {
			timeToDisconnect--;			
		}
		if(timeToDisconnect < 0) {
			internalTemp = ClimateTFC.getActualTemp(pos);
		}
		ticks++;
		if(ticks % CellarsConfig.ICE_BUNKER_TICK_RATE != 0) {
			return;
		}
		
		// determine what modifier to apply
		boolean cold = internalTemp < CellarsConfig.COLD_TEMP;
		boolean frozen = internalTemp < CellarsConfig.FROZEN_TEMP;
		
		for(int i = 0; i < itemStackHandler.getSlots(); i++) {
			IFood cap = itemStackHandler.getStackInSlot(i).getCapability(CapabilityFood.CAPABILITY, null);
            if (cap != null)
            {
            	if(frozen) {
            		CapabilityFood.removeTrait(cap, CellarFoodTrait.COLD);
            		CapabilityFood.applyTrait(cap, CellarFoodTrait.FROZEN);
            	}else if(cold) {
            		CapabilityFood.removeTrait(cap, CellarFoodTrait.FROZEN);
            		CapabilityFood.applyTrait(cap, CellarFoodTrait.COLD);
            	}else {
            		CapabilityFood.removeTrait(cap, CellarFoodTrait.COLD);
            		CapabilityFood.removeTrait(cap, CellarFoodTrait.FROZEN);
            	}
            }
		}
		
	}
	public int getTemperatureInt() {
		int temp = (int)(getTemperature()*1000);
		return temp;
	}
	
}
