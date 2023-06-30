package rbasamoyai.createbigcannons.munitions.autocannon.bullet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;

import javax.annotation.Nullable;

public class MachineGunRoundItem extends Item implements AutocannonAmmoItem {


	public MachineGunRoundItem(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
		return CBCEntityTypes.MACHINE_GUN_BULLET.create(level);
	}

	@Override public boolean isTracer(ItemStack stack) { return stack.getOrCreateTag().getBoolean("Tracer"); }

	@Override public ItemStack getSpentItem(ItemStack stack) { return CBCItems.EMPTY_MACHINE_GUN_ROUND.asStack(); }
}
