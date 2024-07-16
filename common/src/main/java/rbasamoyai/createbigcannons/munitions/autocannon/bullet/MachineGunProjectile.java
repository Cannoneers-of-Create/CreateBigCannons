package rbasamoyai.createbigcannons.munitions.autocannon.bullet;

import javax.annotation.Nonnull;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.autocannon.config.InertAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class MachineGunProjectile extends AbstractAutocannonProjectile {

	public MachineGunProjectile(EntityType<? extends MachineGunProjectile> type, Level level) {
		super(type, level);
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected InertAutocannonProjectileProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE.getPropertiesOf(this);
	}

	@Override public AutocannonAmmoType getAutocannonRoundType() { return AutocannonAmmoType.MACHINE_GUN; }

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		ResourceKey<DamageType> type = entity.isInWater() ? CBCDamageTypes.MACHINE_GUN_FIRE_IN_WATER : CBCDamageTypes.MACHINE_GUN_FIRE;
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(type), this);
	}

}
