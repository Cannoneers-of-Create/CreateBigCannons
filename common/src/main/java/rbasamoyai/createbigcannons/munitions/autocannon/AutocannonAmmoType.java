package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public enum AutocannonAmmoType {
	AUTOCANNON {
		@Override public int getCapacity() { return CBCConfigs.SERVER.munitions.ammoContainerAutocannonRoundCapacity.get(); }
	},
	MACHINE_GUN {
		@Override public int getCapacity() { return CBCConfigs.SERVER.munitions.ammoContainerMachineGunRoundCapacity.get(); }
	},
	NONE {
		@Override public int getCapacity() { return 0; }
	};

	AutocannonAmmoType() {
	}

	public abstract int getCapacity();

	public static AutocannonAmmoType of(ItemStack stack) {
		return stack.getItem() instanceof AutocannonAmmoItem item ? item.getType() : NONE;
	}

}
