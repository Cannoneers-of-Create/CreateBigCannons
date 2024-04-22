package rbasamoyai.createbigcannons.forge.munitions.autocannon;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;

public record AutocannonAmmoContainerInterface(AutocannonAmmoContainerBlockEntity be) implements IItemHandler {

	private static final int AMMO_SLOT = 0;
	private static final int TRACER_SLOT = 1;

	@Override public int getSlots() { return 2; }

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return switch (slot) {
			case AMMO_SLOT -> this.be.getMainAmmoStack();
			case TRACER_SLOT -> this.be.getTracerStack();
			default -> ItemStack.EMPTY;
		};
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (slot != AMMO_SLOT && slot != TRACER_SLOT) return stack;
		AutocannonAmmoType ammoType = this.be.getAmmoType();
		if (!ammoType.isValidMunition(stack)) return stack;

		boolean ammoSlot = slot == AMMO_SLOT;
		ItemStack currentStack = ammoSlot ? this.be.getMainAmmoStack() : this.be.getTracerStack();
		if (!currentStack.isEmpty() && !ItemStack.isSameItemSameTags(stack, currentStack)) return stack;

		int currentCapacity;
		if (ammoType == AutocannonAmmoType.NONE) {
			currentCapacity = AutocannonAmmoType.of(stack).getCapacity();
		} else {
			currentCapacity = ammoSlot ? this.be.getMainAmmoCapacity() : this.be.getTracerAmmoCapacity();
			currentCapacity -= currentStack.getCount();
		}
		int canAdd = Math.min(currentCapacity, stack.getCount());
		if (canAdd < 1) return stack;
		if (!simulate) {
			if (currentStack.isEmpty()) {
				ItemStack copy = stack.copy();
				copy.setCount(canAdd);
				this.be.setItem(slot, copy);
			} else {
				currentStack.grow(canAdd);
			}
			this.be.setChanged();
		}
		ItemStack ret = stack.copy();
		ret.shrink(canAdd);
		return ret;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (slot != AMMO_SLOT && slot != TRACER_SLOT) return ItemStack.EMPTY;

		boolean ammoSlot = slot == AMMO_SLOT;
		ItemStack currentStack = ammoSlot ? this.be.getMainAmmoStack() : this.be.getTracerStack();
		if (currentStack.isEmpty()) return ItemStack.EMPTY;

		int maxRemove = Math.min(currentStack.getCount(), amount);
		if (maxRemove < 1) return ItemStack.EMPTY;
		ItemStack ret = currentStack.copy();
		ret.setCount(maxRemove);
		if (!simulate) {
			currentStack.shrink(maxRemove);
			if (currentStack.isEmpty()) this.be.setItem(slot, ItemStack.EMPTY);
			this.be.setChanged();
		}
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		if (slot != AMMO_SLOT && slot != TRACER_SLOT) return 0;
		return slot == AMMO_SLOT ? this.be.getMainAmmoCapacity() : this.be.getTracerAmmoCapacity();
	}

	@Override public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return this.be.getAmmoType().isValidMunition(stack); }

}
