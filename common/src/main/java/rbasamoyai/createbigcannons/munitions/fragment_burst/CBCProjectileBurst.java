package rbasamoyai.createbigcannons.munitions.fragment_burst;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurst;

public abstract class CBCProjectileBurst extends ProjectileBurst {

	protected CBCProjectileBurst(EntityType<? extends CBCProjectileBurst> entityType, Level level) { super(entityType, level); }

	@Override
	protected void applyForces(double[] velocity, double[] displacement) {
		double length = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1] + velocity[2] * velocity[2]);
		double drag = length < 1e-2d ? 1 : this.getDragCoefficient(length) / length;
		velocity[0] -= velocity[0] * drag;
		velocity[1] -= velocity[1] * drag;
		velocity[2] -= velocity[2] * drag;
		velocity[1] += this.getGravity();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level() instanceof ServerLevel slevel) {
			if (!this.isRemoved() && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
				ChunkPos cpos1 = new ChunkPos(this.blockPosition());
				RitchiesProjectileLib.queueForceLoad(slevel, cpos1.x, cpos1.z);
			}
		}
	}

	protected double getDragCoefficient(double magnitude) {
		BallisticPropertiesComponent properties = this.getProperties().ballistics();
		double drag = properties.drag() * DimensionMunitionPropertiesHandler.getProperties(this.level()).dragMultiplier() * magnitude;
		if (properties.isQuadraticDrag())
			drag *= magnitude;
		return Math.min(drag, magnitude);
	}

	protected double getGravity() {
		BallisticPropertiesComponent properties = this.getProperties().ballistics();
		return properties.gravity() * DimensionMunitionPropertiesHandler.getProperties(this.level()).gravityMultiplier();
	}

	public boolean canHitEntity(Entity target) {
		if (target instanceof AbstractCannonProjectile) // TODO: better check
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
		RandomSource random = level.getRandom();
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

	@Override protected int getLifetime() { return this.getProperties().lifetime(); }

	protected ProjectileBurstProperties getProperties() {
		return CBCMunitionPropertiesHandlers.PROJECTILE_BURST.getPropertiesOf(this);
	}

}
