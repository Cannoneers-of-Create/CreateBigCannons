package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface AutocannonAmmoItem {

	@Nullable AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);

	boolean isTracer(ItemStack stack);

	ItemStack getSpentItem(ItemStack stack);

}
