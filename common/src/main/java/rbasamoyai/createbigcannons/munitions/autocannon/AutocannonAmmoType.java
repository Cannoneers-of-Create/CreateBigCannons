package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.world.item.ItemStack;

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
		return stack.getItem() instanceof AutocannonAmmoItem item ? item.getType() : NONE;
	}

}
