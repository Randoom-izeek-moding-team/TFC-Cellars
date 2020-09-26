package randoom97.cellars.containers;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import randoom97.cellars.registry.CellarFoodTrait;
import randoom97.cellars.tiles.TileCellarShelf;

public class ContainerCellarShelf extends Container {

	private TileCellarShelf te;

	public static final int TEMPERATURE_ID = 0;
	public static final int CONNECTED_ID = 1;

	public ContainerCellarShelf(IInventory playerInventory, TileCellarShelf te) {
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
			for (int col = 0; col < 7; col++) {
				int x = 26 + col * 18;
				int y = row * 18 + 25;
				addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
				slotIndex++;
			}
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (slotId >= 0 && slotId < this.inventorySlots.size()) {
			Slot slot = this.inventorySlots.get(slotId);

			if (slot != null && slot.getHasStack()) {
				ItemStack itemstack = slot.getStack();
				IFood cap = itemstack.getCapability(CapabilityFood.CAPABILITY, null);
				if (cap != null) {
					CapabilityFood.removeTrait(cap, CellarFoodTrait.COLD);
					CapabilityFood.removeTrait(cap, CellarFoodTrait.FROZEN);
				}
			}
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileCellarShelf.SIZE) {
//				IFood cap = itemstack1.getCapability(CapabilityFood.CAPABILITY, null);
//				if(cap != null) {
//					CapabilityFood.removeTrait(cap, CellarFoodTrait.COLD);
//            		CapabilityFood.removeTrait(cap, CellarFoodTrait.FROZEN);
//				}
				if (!this.mergeItemStack(itemstack1, TileCellarShelf.SIZE, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TileCellarShelf.SIZE, false)) {
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
		for (IContainerListener listener : listeners) {
			listener.sendWindowProperty(this, TEMPERATURE_ID, te.getTemperatureInt());
			listener.sendWindowProperty(this, CONNECTED_ID, te.getTimeToDisconnect());
		}
	}

	@Override
	public void updateProgressBar(int id, int data) {
		switch (id) {
		case TEMPERATURE_ID:
			te.setTemperature(data/(float)1000);
			break;
		case CONNECTED_ID:
			te.setTimeToDisconnect(data);
			break;
		}
	}

}
