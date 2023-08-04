package rbasamoyai.createbigcannons.munitions.autocannon.bullet;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

public class MachineGunProjectile extends AbstractAutocannonProjectile {

	public MachineGunProjectile(EntityType<? extends MachineGunProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected DamageSource getEntityDamage() {
		return new MachineGunDamageSource(CreateBigCannons.MOD_ID + ".machine_gun_fire", this, null);
	}

	@Override
	protected float getKnockback(Entity target) {
		float length = this.getDeltaMovement().lengthSqr() > 1e-4d ? 1 : (float) this.getDeltaMovement().lengthSqr();
		return 0.1f / length;
	}

	public static class MachineGunDamageSource extends CannonDamageSource {

		public MachineGunDamageSource(String id, Entity entity, @Nullable Entity owner) {
			super(id, entity, owner);
		}

		@Override
		public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
			String string = "death.attack." + this.msgId;
			if (livingEntity.isInWater()) {
				string += ".in_water";
			}
			return Component.translatable(string, livingEntity.getDisplayName());
		}
	}

}
