package rbasamoyai.createbigcannons.munitions.autocannon;

import javax.annotation.Nullable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

public interface AutocannonAmmoItem {

	@Nullable AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
	@Nullable EntityType<?> getEntityType(ItemStack stack);

	AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack);

	boolean isTracer(ItemStack stack);
	void setTracer(ItemStack stack, boolean value);

	ItemStack getSpentItem(ItemStack stack);

	AutocannonAmmoType getType();

}
