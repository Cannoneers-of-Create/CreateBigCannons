package rbasamoyai.createbigcannons.munitions.autocannon;

import javax.annotation.Nullable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public interface AutocannonAmmoItem {

	@Nullable AbstractAutocannonProjectile<?> getAutocannonProjectile(ItemStack stack, Level level);
	@Nullable EntityType<? extends PropertiesMunitionEntity<? extends AutocannonProjectileProperties>> getEntityType(ItemStack stack);

	boolean isTracer(ItemStack stack);
	void setTracer(ItemStack stack, boolean value);

	ItemStack getSpentItem(ItemStack stack);

	AutocannonAmmoType getType();

}
