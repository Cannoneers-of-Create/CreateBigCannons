package rbasamoyai.createbigcannons.munitions.autocannon.bullet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

public class MachineGunProjectile extends AbstractAutocannonProjectile {

	public MachineGunProjectile(EntityType<? extends MachineGunProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		ResourceKey<DamageType> type = entity.isInWater() ? CBCDamageTypes.MACHINE_GUN_FIRE_IN_WATER : CBCDamageTypes.MACHINE_GUN_FIRE;
		return new CannonDamageSource(this.level().damageSources().damageTypes.getHolderOrThrow(type), this);
	}

	@Override
	protected float getKnockback(Entity target) {
		float length = this.getDeltaMovement().lengthSqr() > 1e-4d ? 1 : (float) this.getDeltaMovement().lengthSqr();
		return 0.1f / length;
	}

}
