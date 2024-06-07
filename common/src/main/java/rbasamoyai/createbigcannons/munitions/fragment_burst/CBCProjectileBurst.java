package rbasamoyai.createbigcannons.munitions.fragment_burst;

import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurst;

public abstract class CBCProjectileBurst<T extends ProjectileBurstProperties> extends ProjectileBurst implements PropertiesMunitionEntity<T> {

	protected CBCProjectileBurst(EntityType<? extends CBCProjectileBurst<?>> entityType, Level level) { super(entityType, level); }

	@Override
	protected void applyForces(double[] velocity) {
		double drag = this.getDrag();
		double gravity = this.getGravity();
		velocity[0] *= drag;
		velocity[1] *= drag;
		velocity[2] *= drag;
		velocity[1] += gravity;
	}

	@Override
	protected int getLifetime() {
		T properties = this.getProperties();
		return properties == null ? 0 : properties.lifetime();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level instanceof ServerLevel slevel) {
			if (!this.isRemoved() && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
				ChunkPos cpos1 = new ChunkPos(this.blockPosition());
				RitchiesProjectileLib.queueForceLoad(slevel, cpos1.x, cpos1.z);
			}
		}
	}

	protected double getDrag() {
		T properties = this.getProperties();
		double baseDrag = properties == null ? 0.99 : properties.drag();
		double scalar = DimensionMunitionPropertiesHandler.getProperties(this.level).dragMultiplier();
		if (scalar <= 1)
			return Mth.lerp(scalar, 1, baseDrag);
		double diff = baseDrag - 1;
		return Mth.clamp(baseDrag + diff * (scalar - 1), 0.9, baseDrag);
	}

	protected double getGravity() {
		T properties = this.getProperties();
		double val = properties == null ? -0.05 : properties.gravity();
		double multiplier = DimensionMunitionPropertiesHandler.getProperties(this.level).gravityMultiplier();
		return val * multiplier;
	}

	public boolean canHitEntity(Entity target) {
		if (target instanceof AbstractCannonProjectile<?>) // TODO: better check
			return false;
		return super.canHitEntity(target);
	}

	public static <T extends ProjectileBurst> T spawnConeBurst(Level level, EntityType<T> type, Vec3 position,
															   Vec3 initialVelocity, int count, double spread) {
		T burst = type.create(level);
		burst.setPos(position);

		Vec3 forward = initialVelocity.normalize();
		Vec3 right = forward.cross(new Vec3(Direction.UP.step()));
		Vec3 up = forward.cross(right);
		double length = initialVelocity.length();
		Random random = level.getRandom();
		for (int i = 0; i < count; ++i) {
			double velScale = length * (1.4d + 0.2d * random.nextDouble());
			double rx = (random.nextDouble() - random.nextDouble()) * 0.0625d;
			double ry = (random.nextDouble() - random.nextDouble()) * 0.0625d;
			double rz = (random.nextDouble() - random.nextDouble()) * 0.0625d;
			Vec3 vel = forward.scale(velScale)
				.add(right.scale((random.nextDouble() - random.nextDouble()) * velScale * spread))
				.add(up.scale((random.nextDouble() - random.nextDouble()) * velScale * spread));
			burst.addSubProjectile(rx, ry, rz, vel.x, vel.y, vel.z);
		}
		if (burst.getSubProjectileCount() != count) {
			CreateBigCannons.LOGGER.info("Projectile burst failed to spawn {} out of {} projectiles",
				count - burst.getSubProjectileCount(), count);
		}
		level.addFreshEntity(burst);
		return burst;
	}

}
