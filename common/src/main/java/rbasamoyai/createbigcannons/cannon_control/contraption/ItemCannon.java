package rbasamoyai.createbigcannons.cannon_control.contraption;

import net.minecraft.world.item.ItemStack;

public interface ItemCannon {

	ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate);
	ItemStack extractItemFromCannon(boolean simulate);

}
