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
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;

public class MachineGunProjectile extends AbstractAutocannonProjectile<AutocannonProjectileProperties> {

	public MachineGunProjectile(EntityType<? extends MachineGunProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		ResourceKey<DamageType> type = entity.isInWater() ? CBCDamageTypes.MACHINE_GUN_FIRE_IN_WATER : CBCDamageTypes.MACHINE_GUN_FIRE;
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(type), this);
	}

}
