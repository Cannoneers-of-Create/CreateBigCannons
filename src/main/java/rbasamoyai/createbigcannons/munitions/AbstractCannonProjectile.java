package rbasamoyai.createbigcannons.munitions;

import com.mojang.math.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions.GriefState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractCannonProjectile extends Projectile {

	protected static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> PROJECTILE_MASS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.FLOAT);
	protected int inGroundTime = 0;
	protected float damage = 50;
	
	protected AbstractCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
		if (this.level.isClientSide || this.level.hasChunkAt(this.blockPosition())) {
			super.tick();

			if (!this.isInGround()) this.clipAndDamage();

			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();

			if (!this.isInGround()) {
				Vec3 vel = this.getDeltaMovement();
				if (vel.lengthSqr() > 0.005d) {
					this.setYRot((float) (Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
					this.setXRot((float) (Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
				}

				this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
				this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
			}

			if (this.isInGround()) {
				this.setDeltaMovement(Vec3.ZERO);
				if (this.shouldFall()) {
					this.setInGround(false);
				} else if (!this.level.isClientSide) {
					this.inGroundTime++;

					if (this.inGroundTime == 400) {
						this.discard();
					}
				}
			} else {
				this.inGroundTime = 0;
				Vec3 uel = this.getDeltaMovement();
				Vec3 vel = uel;
				Vec3 oldPos = this.position();
				Vec3 newPos = oldPos.add(vel);
				if (!this.isNoGravity()) vel = vel.add(0.0f, this.getGravity(), 0.0f);
				vel = vel.scale(this.getDrag());
				this.setDeltaMovement(vel);
				this.setPos(newPos.add(vel.subtract(uel).scale(0.5)));

				ParticleOptions particle = this.getTrailParticles();
				if (particle != null) {
					for (int i = 0; i < 10; ++i) {
						double partial = i * 0.1f;
						double dx = Mth.lerp(partial, this.xOld, this.getX());
						double dy = Mth.lerp(partial, this.yOld, this.getY());
						double dz = Mth.lerp(partial, this.zOld, this.getZ());
						this.level.addParticle(particle, dx, dy, dz, 0.0d, 0.0d, 0.0d);
					}
				}
			}
		}
	}

	protected void clipAndDamage() {
		Vec3 start = this.position();
		Vec3 end = start.add(this.getDeltaMovement());
		GriefState flag = CBCConfigs.SERVER.munitions.damageRestriction.get();

		double reach = Math.max(this.getBbWidth(), this.getBbHeight()) * 0.5;

		AABB generalMovementRegion = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1);
		List<Entity> hitEntities = new ArrayList<>();
		for (Entity target : this.level.getEntities(this, generalMovementRegion, this::canHitEntity)) {
			AABB targetBox = target.getBoundingBox().inflate(reach);
			if (targetBox.clip(start, end).isPresent()) hitEntities.add(target);
		}

		ProjectileContext projCtx = new ProjectileContext(this, start, end, hitEntities, flag);

		BiFunction<ProjectileContext, BlockPos, Boolean> onClip = this::onClip;
		BlockGetter.traverseBlocks(start, end, projCtx, onClip.andThen(b -> b ? 1 : null), fPos -> null);

		if (!this.level.isClientSide && flag != GriefState.NO_DAMAGE) {
			Vec3 oldVel = this.getDeltaMovement();
			for (Map.Entry<BlockPos, Float> explosion : projCtx.getQueuedExplosions().entrySet()) {
				BlockPos pos = explosion.getKey();
				this.level.explode(this, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, explosion.getValue(), Explosion.BlockInteraction.DESTROY);
			}
			this.setDeltaMovement(oldVel);
		}
	}

	/**
	 * @param ctx
	 * @param pos
	 * @return true to stop iterating, false otherwise
	 */
	protected boolean onClip(ProjectileContext ctx, BlockPos pos) {
		BlockState state = this.level.getChunkAt(pos).getBlockState(pos);
		VoxelShape vshape = state.getCollisionShape(this.level, pos, ctx.collisionContext());
		BlockHitResult bResult = this.level.clipWithInteractionOverride(ctx.start(), ctx.end(), pos, vshape, state);
		Vec3 hitLoc = bResult == null ? Vec3.atBottomCenterOf(pos) : bResult.getLocation();

		Vec3 curVel = this.getDeltaMovement();
		double mag = curVel.length();
		double reach = Math.max(this.getBbWidth(), this.getBbHeight()) * 0.5;

		if (!ctx.hitEntities().isEmpty()) {
			AABB currentMovementRegion = this.getBoundingBox()
					.expandTowards(curVel.scale(1.75 / mag)) // 1.75 ~ sqrt 3
					.inflate(1)
					.move(hitLoc.subtract(this.position()));
			for (Iterator<Entity> targetIter = ctx.hitEntities().iterator(); targetIter.hasNext(); ) {
				Entity target = targetIter.next();
				if (currentMovementRegion.intersects(target.getBoundingBox().inflate(reach))) {
					this.onHitEntity(target);
					targetIter.remove();
				}
			}
		}

		if (bResult != null) {
			boolean flag1 = ctx.getLastState().isAir();
			if (flag1 && this.tryBounceOffBlock(state, bResult)) return true;
			ctx.setLastState(state);

			double startMass = this.getProjectileMass();
			double curPom = startMass * mag;
			double hardness = BlockHardnessHandler.getHardness(state);

			if (ctx.griefState() == GriefState.NO_DAMAGE || state.getDestroySpeed(this.level, pos) == -1 || curPom < hardness) {
				this.setPos(hitLoc.add(curVel.scale(0.03 / mag)));
				this.setInGround(true);
				this.setDeltaMovement(Vec3.ZERO);
				this.onFinalImpact(bResult);
				return true;
			}
			this.onDestroyBlock(state, bResult);

			double f = this.overPenetrationPower(hardness, curPom);
			if (flag1 && f > 0) {
				ctx.queueExplosion(pos.immutable(), (float) f);
			}
		}

		return this.isRemoved();
	}

	protected double overPenetrationPower(double hardness, double curPom) {
		double f = hardness / curPom;
		return f <= 0.15 ? 2 - 2 * f : 0;
	}

	protected boolean tryBounceOffBlock(BlockState state, BlockHitResult result) {
		BounceType bounce = this.canBounce(state, result);
		if (bounce == BounceType.NO_BOUNCE) return false;
		Vec3 oldVel = this.getDeltaMovement();
		double momentum = this.getProjectileMass() * oldVel.length();
		if (bounce == BounceType.DEFLECT) {
			if (momentum > BlockHardnessHandler.getHardness(state) * 0.5) {
				Vec3 spallLoc = this.position().add(oldVel.normalize().scale(2));
				this.level.explode(null, spallLoc.x, spallLoc.y, spallLoc.z, 2, Explosion.BlockInteraction.NONE);
			}
			SoundType sound = state.getSoundType();
			this.playSound(sound.getBreakSound(), sound.getVolume(), sound.getPitch());
		}
		Vec3 normal = new Vec3(result.getDirection().step());
		double elasticity = bounce == BounceType.RICOCHET ? 1.5d : 1.9d;
		this.setDeltaMovement(oldVel.subtract(normal.scale(normal.dot(oldVel) * elasticity)));
		return true;
	}

	/** Use for fuzes and any other final effects */
	protected void onFinalImpact(HitResult result) {}

	protected abstract void onDestroyBlock(BlockState state, BlockHitResult result);

	protected void onHitEntity(Entity entity) {
		if (this.getProjectileMass() <= 0) return;
		if (!this.level.isClientSide) {
			entity.setDeltaMovement(this.getDeltaMovement().scale(this.getKnockback(entity)));
			DamageSource source = this.getEntityDamage();
			entity.hurt(source, this.damage);
			if (!CBCConfigs.SERVER.munitions.invulProjectileHurt.get()) entity.invulnerableTime = 0;
			double penalty = entity.isAlive() ? 2 : 0.2;
			this.setProjectileMass((float) Math.max(this.getProjectileMass() - penalty, 0));
			if (this.getProjectileMass() <= 0) {
				this.onFinalImpact(new EntityHitResult(entity));
				this.discard();
			}
		}
	}

	protected DamageSource getEntityDamage() {
		return DamageSource.thrown(this, null).bypassArmor();
	}

	protected float getKnockback(Entity target) { return 2.0f; }

	protected boolean canDeflect(BlockHitResult result) { return false; }
	protected boolean canBounceOffOf(BlockState state) { return isBounceableOffOf(state); }

	public static boolean isDeflector(BlockState state) {
		if (state.is(CBCTags.BlockCBC.DEFLECTS_SHOTS)) return true;
		if (state.is(CBCTags.BlockCBC.DOESNT_DEFLECT_SHOTS)) return false;
		Material material = state.getMaterial();
		return material == Material.METAL || material == Material.HEAVY_METAL;
	}

	public static boolean isBounceableOffOf(BlockState state) {
		if (state.is(CBCTags.BlockCBC.DOESNT_BOUNCE_SHOTS)) return false;
		if (state.is(CBCTags.BlockCBC.BOUNCES_SHOTS)) return true;
		Material material = state.getMaterial();
		return material.isSolidBlocking() && material.getPushReaction() != PushReaction.DESTROY;
	}

	protected BounceType canBounce(BlockState state, BlockHitResult result) {
		if (!CBCConfigs.SERVER.munitions.projectilesCanBounce.get() || this.getProjectileMass() <= 0) return BounceType.NO_BOUNCE;

		if (!this.canBounceOffOf(state)) return BounceType.NO_BOUNCE;

		Vec3 oldVel = this.getDeltaMovement();
		double mag = oldVel.length();
		if (mag < 0.2) return BounceType.NO_BOUNCE;
		Vec3 normal = new Vec3(result.getDirection().step());
		double fc = normal.dot(oldVel) / mag;
		if (this.canDeflect(result) && -1 <= fc && fc <= -0.5) return BounceType.DEFLECT; // cos 180 <= fc <= cos 120
		return -0.5 <= fc && fc <= 0 ? BounceType.RICOCHET : BounceType.NO_BOUNCE; // cos 120 <= fc <= cos 90
	}

	@Override public boolean hurt(DamageSource source, float damage) { return false; }

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ID_FLAGS, (byte) 0);
		this.entityData.define(PROJECTILE_MASS, 0.0f);
	}
	
	public void setInGround(boolean inGround) {
		if (inGround) {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) | 1));
		} else {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) & 0b11111110));
		}
	}
	
	public boolean isInGround() {
		return (this.entityData.get(ID_FLAGS) & 1) != 0;
	}
	
	private boolean shouldFall() {
		return this.isInGround() && this.level.noCollision(new AABB(this.position(), this.position()).inflate(0.06d));
	}

	@Nullable protected ParticleOptions getTrailParticles() { return null; }
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("ProjectileMass", this.getProjectileMass());
		tag.putBoolean("InGround", this.isInGround());
		tag.putFloat("Damage", this.damage);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setProjectileMass(tag.getFloat("ProjectileMass"));
		this.setInGround(tag.getBoolean("InGround"));
		this.damage = tag.getFloat("Damage");
	}
	
	public void setProjectileMass(float power) {
		this.entityData.set(PROJECTILE_MASS, power);
	}
	
	public float getProjectileMass() {
		return CBCConfigs.SERVER.munitions.damageRestriction.get() == GriefState.NO_DAMAGE ? 0 : this.entityData.get(PROJECTILE_MASS);
	}

	public static void build(EntityType.Builder<? extends AbstractCannonProjectile> builder) {
		builder.setTrackingRange(16)
				.setUpdateInterval(1)
				.setShouldReceiveVelocityUpdates(true)
				.fireImmune()
				.sized(0.8f, 0.8f);
	}
	
	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5f;
	}

	protected float getGravity() { return -0.05f; }
	protected float getDrag() { return 0.99f; }

	public void setChargePower(float power) {}

	@Override public boolean canHitEntity(Entity entity) { return super.canHitEntity(entity) && !(entity instanceof Projectile); }

	public enum BounceType {
		DEFLECT,
		RICOCHET,
		NO_BOUNCE
	}

}
