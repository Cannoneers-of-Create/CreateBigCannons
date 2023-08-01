package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.bullet.MachineGunRoundItem;

public enum AutocannonAmmoType {
	AUTOCANNON {
		@Override public int getCapacity() { return 16; }
	},
	MACHINE_GUN {
		@Override public int getCapacity() { return 64; }
	},
	NONE {
		@Override public int getCapacity() { return 0; }
	};

	AutocannonAmmoType() {
	}

	public abstract int getCapacity();

	public static AutocannonAmmoType of(ItemStack stack) {
		if (stack.getItem() instanceof AutocannonCartridgeItem) return AUTOCANNON;
		if (stack.getItem() instanceof MachineGunRoundItem) return MACHINE_GUN;
		return NONE;
	}

}
