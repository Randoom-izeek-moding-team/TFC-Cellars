package randoom97.cellars.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import randoom97.cellars.tiles.TileBasicElectricCooler;

public class ContainerBasicElectricCooler extends Container {

	private TileBasicElectricCooler te;

	public static final int TEMPERATURE_ID = 0;
	public static final int DOORS_FOUND_ID = 1;
	public static final int DOORS_OPEN_ID = 2;
	public static final int FORMED_ID = 3;
	public static final int ENERGY_ID = 4;
	
	
	public ContainerBasicElectricCooler(IInventory playerInventory, TileBasicElectricCooler te) {
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		// slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = row * 18 + 84;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}

		// slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			int y = 58 + 84;
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		// add our own slots
		int slotIndex = 0;
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				int x = 71 + col * 18;
				int y = row * 18 + 25;
				addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
				slotIndex++;
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileBasicElectricCooler.SIZE) {
				if (!this.mergeItemStack(itemstack1, TileBasicElectricCooler.SIZE, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TileBasicElectricCooler.SIZE, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for(IContainerListener listener : listeners) {
			listener.sendWindowProperty(this, ENERGY_ID, te.getEnergyStored());
			listener.sendWindowProperty(this, TEMPERATURE_ID, Float.floatToIntBits(te.getTemperature()));
			listener.sendWindowProperty(this, DOORS_FOUND_ID, te.getDoorsFound() ? 1 : 0);
			listener.sendWindowProperty(this, DOORS_OPEN_ID, te.getDoorsOpen() ? 1 : 0);
			listener.sendWindowProperty(this, FORMED_ID, te.getFormed() ? 1 : 0);
		}
	}
	
	@Override
	public void updateProgressBar(int id, int data) {
		switch(id) {
		case TEMPERATURE_ID:
			te.setTemperature(Float.intBitsToFloat(data));
			break;
		case DOORS_FOUND_ID:
			te.setDoorsFound(data == 1);
			break;
		case DOORS_OPEN_ID:
			te.setDoorsOpen(data == 1);
			break;
		case FORMED_ID:
			te.setFormed(data == 1);
			break;
		case ENERGY_ID:
			te.setEnergyStored(data);
			break;
		}
	
	}

}
