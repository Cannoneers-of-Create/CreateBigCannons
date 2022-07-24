package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzeItemHandler implements IItemHandlerModifiable {

	private final FuzedBlockEntity be;
	
	public FuzeItemHandler(FuzedBlockEntity be) {
		this.be = be;
	}
	
	@Override public int getSlots() { return 1; }
	@Override public ItemStack getStackInSlot(int slot) { return slot == 0 ? this.be.getFuze() : ItemStack.EMPTY; }

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!this.be.getFuze().isEmpty()) return stack;
		ItemStack result = stack.copy();
		if (!simulate) this.be.setFuze(result.split(1));
		return result;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (this.be.getFuze().isEmpty()) return ItemStack.EMPTY;
		if (simulate) {
			ItemStack copy = this.be.getFuze().copy();
			copy.setCount(Math.min(amount, copy.getCount()));
			return copy;
		}
		return this.be.getFuze().split(amount);
	}

	@Override public int getSlotLimit(int slot) { return 1; }
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return slot == 0 && !stack.isEmpty() && stack.getItem() instanceof FuzeItem && this.be.getFuze().isEmpty();
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if (this.isItemValid(slot, stack)) this.be.setFuze(stack);
	}

}
