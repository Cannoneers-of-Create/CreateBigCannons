package rbasamoyai.createbigcannons.forge.cannons;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;

public record AutocannonBreechInterface(AbstractAutocannonBreechBlockEntity breech) implements IItemHandler {
	@Override
	public int getSlots() {
		return 2;
	}

	@NotNull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return switch (slot) {
			case 0 -> this.breech.getOutputBuffer();
			case 1 -> this.breech.isInputFull() ? this.breech.getInputBuffer().peekLast() : ItemStack.EMPTY;
			default -> ItemStack.EMPTY;
		};
	}

	@NotNull
	@Override
	public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
		if (slot != 1 || !this.isItemValid(slot, stack) || this.breech.isInputFull()) return stack;
		int maxCount = Math.min(this.breech.getQueueLimit() - this.breech.getInputBuffer().size(), stack.getCount());
		if (!simulate) {
			for (int i = 0; i < maxCount; ++i) {
				this.breech.getInputBuffer().add(ItemHandlerHelper.copyStackWithSize(stack, 1));
			}
		}
		return stack.getCount() == maxCount ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - maxCount);
	}

	@NotNull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount <= 0) return ItemStack.EMPTY;
		return switch (slot) {
			case 0 ->
					simulate ? ItemHandlerHelper.copyStackWithSize(this.breech.getOutputBuffer(), 1) : this.breech.getOutputBuffer().split(1);
			default -> ItemStack.EMPTY;
		};
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, @NotNull ItemStack stack) {
		return stack.getItem() instanceof AutocannonCartridgeItem;
	}
}
