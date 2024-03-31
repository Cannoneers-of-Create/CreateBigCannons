package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

public class AutocannonAmmoContainerMenuSlot extends Slot {

	private final IAutocannonAmmoContainerContainer ammoContainer;
	private final boolean isCreative;

	public AutocannonAmmoContainerMenuSlot(IAutocannonAmmoContainerContainer container, int slot, int x, int y, boolean isCreative) {
		super(container, slot, x, y);
		this.ammoContainer = container;
		this.isCreative = isCreative;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		AutocannonAmmoType placeType = AutocannonAmmoType.of(stack);
		AutocannonAmmoType type = this.ammoContainer.getAmmoType();
		return placeType != AutocannonAmmoType.NONE && type == AutocannonAmmoType.NONE ||
			placeType == type && this.ammoContainer.getTotalCount() < type.getCapacity();
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		if (this.isCreative) return 1;
		AutocannonAmmoType ctType = this.ammoContainer.getAmmoType();
		if (ctType == AutocannonAmmoType.NONE) return AutocannonAmmoType.of(stack).getCapacity();
		int buf = Math.max(ctType.getCapacity() - this.ammoContainer.getTotalCount(), 0);
		ItemStack item = this.ammoContainer.getItem(this.getContainerSlot());
		return Math.min(item.getCount() + buf, item.getMaxStackSize());
	}

}
