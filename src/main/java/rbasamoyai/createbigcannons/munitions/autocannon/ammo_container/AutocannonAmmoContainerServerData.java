package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import net.minecraft.world.inventory.ContainerData;

public class AutocannonAmmoContainerServerData implements ContainerData {

	@Nullable private final AutocannonAmmoContainerBlockEntity be;

	public AutocannonAmmoContainerServerData(@Nullable AutocannonAmmoContainerBlockEntity be) {
		this.be = be;
	}

	@Override
	public int get(int index) {
		return this.be != null && index == 0 ? this.be.getSpacing() : 1;
	}

	@Override
	public void set(int index, int value) {
		if (this.be != null && index == 0) this.be.setSpacing(value);
	}

	@Override public int getCount() { return 1; }

}
