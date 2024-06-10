package rbasamoyai.createbigcannons.munitions.autocannon.bullet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.config.InertAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class MachineGunProjectile extends AbstractAutocannonProjectile {

	public MachineGunProjectile(EntityType<? extends MachineGunProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected DamageSource getEntityDamage() {
		return new MachineGunDamageSource(CreateBigCannons.MOD_ID + ".machine_gun_fire", this, null, this.getDamageProperties().ignoresEntityArmor());
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

	public static class MachineGunDamageSource extends CannonDamageSource {
		public MachineGunDamageSource(String id, Entity entity, @Nullable Entity owner, boolean bypassArmor) {
			super(id, entity, owner, bypassArmor);
		}

		@Override
		public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
			String string = "death.attack." + this.msgId;
			if (livingEntity.isInWater()) string += ".in_water";
			return Components.translatable(string, livingEntity.getDisplayName());
		}
	}

}
