package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class ImpactFuzeItem extends FuzeItem {

	public ImpactFuzeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean onProjectileImpact(ItemStack stack, FuzedCannonProjectile projectile, HitResult result) {
		return projectile.getBreakthroughPower() > 0 ? false : projectile.level.getRandom().nextFloat() < 0.67f;
	}
	
}
