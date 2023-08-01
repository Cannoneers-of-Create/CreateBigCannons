package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AmmoContainerContainer implements Container {

	public static final int AMMO_SLOT = 0;
	public static final int TRACER_SLOT = 1;

	private final ItemStack stack;

	public AmmoContainerContainer(ItemStack stack) {
		this.stack = stack;
	}

	@Override public int getContainerSize() { return 2; }

	@Override
	public boolean isEmpty() {
		return this.getItem(AMMO_SLOT).isEmpty() && this.getItem(TRACER_SLOT).isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return switch (slot) {
			case 0 -> AmmoContainerItem.getMainAmmoStack(this.stack);
			case 1 -> AmmoContainerItem.getTracerAmmoStack(this.stack);
			default -> ItemStack.EMPTY;
		};
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (amount <= 0 || slot != 0 && slot != 1) return ItemStack.EMPTY;
		ItemStack ammo = this.getItem(slot);
		if (ammo.isEmpty()) return ItemStack.EMPTY;
		ItemStack split = ammo.split(amount);
		this.stack.getOrCreateTag().put(slot == AMMO_SLOT ? "Ammo" : "Tracers", ammo.save(new CompoundTag()));
		this.setChanged();
		return split;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot != 0 && slot != 1) return ItemStack.EMPTY;
		ItemStack ret = this.getItem(slot);
		this.stack.getOrCreateTag().put(slot == AMMO_SLOT ? "Ammo" : "Tracers", ItemStack.EMPTY.save(new CompoundTag()));
		return ret;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot != 0 && slot != 1) return;
		this.stack.getOrCreateTag().put(slot == AMMO_SLOT ? "Ammo" : "Tracers", stack.save(new CompoundTag()));
		this.setChanged();
	}

	@Override
	public void setChanged() {

	}

	@Override public boolean stillValid(Player player) { return true; }

	@Override
	public void clearContent() {
		CompoundTag tag = this.stack.getOrCreateTag();
		tag.put("Ammo", ItemStack.EMPTY.save(new CompoundTag()));
		tag.put("Tracers", ItemStack.EMPTY.save(new CompoundTag()));
	}

	public int getTotalCount() { return this.getItem(AMMO_SLOT).getCount() + this.getItem(TRACER_SLOT).getCount(); }

	public AutocannonAmmoType getType() {
		AutocannonAmmoType type = AutocannonAmmoType.of(this.getItem(AMMO_SLOT));
		if (type != AutocannonAmmoType.NONE) return type;
		return AutocannonAmmoType.of(this.getItem(TRACER_SLOT));
	}

}
