package rbasamoyai.createbigcannons.munitions;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.SyncsExtraDataOnAdd;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions.GriefState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

public abstract class AbstractCannonProjectile extends Projectile implements SyncsExtraDataOnAdd {

	protected static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> PROJECTILE_MASS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.FLOAT);
	protected int inGroundTime = 0;
	protected float damage;
	protected int inFluidTime = 0;
	protected int penetrationTime = 0;
	@Nullable protected Vec3 nextVelocity = null;
	@Nullable protected Vec3 orientation = null;
	protected BlockState lastPenetratedBlock = Blocks.AIR.defaultBlockState();
	protected boolean removeNextTick = false;

	protected AbstractCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) {
		super(type, level);
		this.damage = this.getDamageProperties().entityDamage();
		this.setProjectileMass(this.getBallisticProperties().durabilityMass());
	}

	@Nonnull public abstract EntityDamagePropertiesComponent getDamageProperties();

	@Override
	public void tick() {
		if (this.removeNextTick) {
			this.discard();
			return;
		}
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			super.tick();

			if (this.nextVelocity != null) {
				this.setDeltaMovement(this.nextVelocity);
				if (this.nextVelocity.lengthSqr() < 1e-4d)
					this.setInGround(true);
				this.nextVelocity = null;
			}

			if (!this.isInGround()) {
				this.clipAndDamage();
				this.orientation = this.getDeltaMovement();
			}

			this.onTickRotate();

			if (this.isInGround()) {
				this.setDeltaMovement(Vec3.ZERO);
				if (!this.level.isClientSide) {
					if (this.shouldFall()) {
						this.setInGround(false);
					} else if (!this.canLingerInGround()) {
						this.inGroundTime++;
						if (this.inGroundTime == 400) {
							this.discard();
						}
					}
				}
			} else {
				this.inGroundTime = 0;
				Vec3 oldVel = this.getDeltaMovement();
				Vec3 oldPos = this.position();
				if (this.nextVelocity != null) {
					Vec3 newPos = oldPos.add(oldVel);
					this.setPos(newPos);
				} else {
					Vec3 accel = this.getForces(oldPos, oldVel);
					Vec3 newPos = oldPos.add(oldVel).add(accel.scale(0.5));
					this.setPos(newPos);
					this.setDeltaMovement(oldVel.add(accel));
				}
				this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
				this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
			}

			if (this.inFluidTime > 0)
				--this.inFluidTime;
			if (this.penetrationTime > 0)
				--this.penetrationTime;

			if (this.level instanceof ServerLevel slevel && !this.isRemoved()) {
				if (CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
					ChunkPos cpos1 = new ChunkPos(this.blockPosition());
					RitchiesProjectileLib.queueForceLoad(slevel, cpos1.x, cpos1.z);
				}
			}
		}
	}

	protected void onTickRotate() {}

	protected Vec3 getForces(Vec3 position, Vec3 velocity) {
		return velocity.normalize().scale(-this.getDragForce())
			.add(0.0d, this.getGravity(), 0.0d);
	}

	public Vec3 getOrientation() {
		return this.orientation == null ? this.getDeltaMovement() : this.orientation;
	}

	@Override
	public void lerpTo(double x, double y, double z, float yRot, float xRot, int lerpSteps, boolean teleport) {
		if (this.tickCount < 2)
			return;
		super.lerpTo(x, y, z, yRot, xRot, lerpSteps, teleport);
	}

	protected void clipAndDamage() {
		ProjectileContext projCtx = new ProjectileContext(this, CBCConfigs.SERVER.munitions.damageRestriction.get());

		Vec3 pos = this.position();
		Vec3 currentStart = pos;
		double reach = Math.max(this.getBbWidth(), this.getBbHeight()) * 0.5;

		double t = 1;
		int MAX_ITER = 100;
		Vec3 originalVel = this.getDeltaMovement();
		Vec3 accel = this.getForces(pos, originalVel);
		Vec3 trajectory = originalVel.add(accel);
		Vec3 finalEnd = pos.add(trajectory);
		double velMag = trajectory.length();
		double vmRecip = 1 / velMag;

		boolean shouldRemove = false;
		boolean stop = false;

		for (int p = 0; p < MAX_ITER; ++p) {
			if (velMag * t < 1e-2d) {
				this.lastPenetratedBlock = Blocks.AIR.defaultBlockState();
				break;
			}

			Vec3 currentEnd = currentStart.add(trajectory.scale(t));
			BlockHitResult blockResult = this.level.clip(new ClipContext(currentStart, currentEnd, ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE, this));
			if (blockResult.getType() != HitResult.Type.MISS)
				currentEnd = blockResult.getLocation();
			if (this.onClip(projCtx, currentStart, currentEnd)) {
				shouldRemove = true;
				break;
			}

			Vec3 disp = currentEnd.subtract(currentStart);
			AABB currentMovementRegion = this.getBoundingBox().expandTowards(disp).inflate(1).move(currentStart.subtract(pos));

			Vec3 endStart = currentStart;
			Vec3 endCopy = currentEnd;
			AABB thisBB = this.getBoundingBox();
			for (Entity target : this.level.getEntities(this, currentMovementRegion)) {
				if (projCtx.hasHitEntity(target) || !this.canHitEntity(target))
					continue;
				AABB targetBB = target.getBoundingBox();
				if (targetBB.intersects(thisBB) || targetBB.inflate(reach).clip(endStart, endCopy).isPresent())
					projCtx.addEntity(target);
			}

			currentStart = currentEnd;
			t -= disp.length() * vmRecip;
			if (blockResult.getType() != HitResult.Type.MISS) {
				BlockPos bpos = blockResult.getBlockPos().immutable();
				BlockState state = this.level.getChunkAt(bpos).getBlockState(bpos);

				ImpactResult result = this.calculateBlockPenetration(projCtx, state, blockResult);
				switch (result.kinematics) {
					case PENETRATE -> {
						this.lastPenetratedBlock = state;
						this.penetrationTime = 2;
					}
					case BOUNCE -> {
						this.setDeltaMovement(trajectory.scale(1 - t));
						Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level, blockResult);
						double elasticity = 1.7f;
						this.nextVelocity = originalVel.subtract(normal.scale(normal.dot(originalVel) * elasticity));
						stop = true;
					}
					case STOP -> {
						this.setDeltaMovement(trajectory.scale(1 - t));
						this.nextVelocity = Vec3.ZERO;
						this.lastPenetratedBlock = state;
						this.penetrationTime = 2;
						stop = true;
					}
				}
				shouldRemove |= result.shouldRemove;
			}
			if (shouldRemove || stop || t <= 0)
				break;
		}

		BlockHitResult fluidResult = this.level.clip(new ClipContext(pos, finalEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));
		if (fluidResult.getType() != HitResult.Type.MISS) {
			BlockPos fluidPos = fluidResult.getBlockPos();
			BlockState state = this.level.getBlockState(fluidPos);
			if (state.getBlock() instanceof LiquidBlock) {
				if (this.inFluidTime <= 0) {
					Vec3 effectNormal = originalVel.reverse();
					Vec3 fluidExplosionPos = fluidResult.getLocation();
					projCtx.addPlayedEffect(new ClientboundPlayBlockHitEffectPacket(state, this.getType(), true, false,
						fluidExplosionPos.x, fluidExplosionPos.y, fluidExplosionPos.z, (float) effectNormal.x,
						(float) effectNormal.y, (float) effectNormal.z));
				}
				this.inFluidTime = 5;
			}
		}

		for (Entity e : projCtx.hitEntities())
			this.onHitEntity(e, projCtx);

		if (!this.level.isClientSide) {
			if (projCtx.griefState() != GriefState.NO_DAMAGE) {
				Vec3 oldVel = this.getDeltaMovement();
				for (Map.Entry<BlockPos, Float> queued : projCtx.getQueuedExplosions().entrySet()) {
					BlockPos bpos = queued.getKey();
					ImpactExplosion explosion = new ImpactExplosion(this.level, this, this.indirectArtilleryFire(), bpos.getX() + 0.5,
						bpos.getY() + 0.5, bpos.getZ() + 0.5, queued.getValue(), Explosion.BlockInteraction.DESTROY);
					CreateBigCannons.handleCustomExplosion(this.level, explosion);
				}
				this.setDeltaMovement(oldVel);
			}
			for (ClientboundPlayBlockHitEffectPacket pkt : projCtx.getPlayedEffects())
				NetworkPlatform.sendToClientTracking(pkt, this);
		}
		if (shouldRemove)
			this.removeNextTick = true;
	}

	protected boolean canHitSurface() {
		return this.lastPenetratedBlock.isAir() && this.penetrationTime == 0;
	}

	/**
	 * @param ctx
	 * @param start
	 * @param end
	 * @return true to remove the entity, false otherwise
	 */
	protected boolean onClip(ProjectileContext ctx, Vec3 start, Vec3 end) {
		return false;
	}

	/**
	 * Damage the impacted block.
	 *
	 * @param projectileContext
	 * @param state
	 * @param blockHitResult
	 * @return
	 */
	protected abstract ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult);

	protected void onHitEntity(Entity entity, ProjectileContext projectileContext) {
		if (this.getProjectileMass() <= 0) return;
		if (!this.level.isClientSide) {
			EntityDamagePropertiesComponent properties = this.getDamageProperties();
			entity.setDeltaMovement(this.getDeltaMovement().scale(this.getKnockback(entity)));
			DamageSource source = this.getEntityDamage();

			if (properties == null || properties.ignoresInvulnerability()) entity.invulnerableTime = 0;
			entity.hurt(source, this.damage);
			if (properties == null || !properties.rendersInvulnerable()) entity.invulnerableTime = 0;

			float penalty = entity.isAlive() ? 2f : 0.2f;
			this.setProjectileMass(Math.max(this.getProjectileMass() - penalty, 0));
		}
	}

	protected DamageSource getEntityDamage() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".cannon_projectile", this, null, this.getDamageProperties().ignoresEntityArmor());
	}

	protected float getKnockback(Entity target) {
		return this.getDamageProperties().knockback();
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

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("ProjectileMass", this.getProjectileMass());
		tag.putBoolean("InGround", this.isInGround());
		tag.putFloat("Damage", this.damage);
		if (this.nextVelocity != null)
			tag.put("NextMotion", this.newDoubleList(this.nextVelocity.x, this.nextVelocity.y, this.nextVelocity.z));
		if (this.orientation != null)
			tag.put("Orientation", this.newDoubleList(this.orientation.x, this.orientation.y, this.orientation.z));
		tag.put("LastPenetration", NbtUtils.writeBlockState(this.lastPenetratedBlock));
		if (this.removeNextTick)
			tag.putBoolean("RemoveNextTick", true);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setProjectileMass(tag.getFloat("ProjectileMass"));
		this.setInGround(tag.getBoolean("InGround"));
		this.damage = tag.getFloat("Damage");
		ListTag motionTag = tag.getList("Motion", Tag.TAG_DOUBLE);
		this.setDeltaMovement(motionTag.getDouble(0), motionTag.getDouble(1), motionTag.getDouble(2));
		if (tag.contains("NextMotion", Tag.TAG_LIST)) {
			ListTag nextMotion = tag.getList("NextMotion", Tag.TAG_DOUBLE);
			this.nextVelocity = nextMotion.size() == 3 ? new Vec3(nextMotion.getDouble(0), nextMotion.getDouble(1), nextMotion.getDouble(2)) : null;
		} else {
			this.nextVelocity = null;
		}
		if (tag.contains("Orientation", Tag.TAG_LIST)) {
			ListTag nextMotion = tag.getList("Orientation", Tag.TAG_DOUBLE);
			this.orientation = nextMotion.size() == 3 ? new Vec3(nextMotion.getDouble(0), nextMotion.getDouble(1), nextMotion.getDouble(2)) : null;
		} else {
			this.orientation = this.getDeltaMovement();
		}
		this.lastPenetratedBlock = tag.contains("LastPenetration", Tag.TAG_COMPOUND)
			? NbtUtils.readBlockState(tag.getCompound("LastPenetration"))
			: Blocks.AIR.defaultBlockState();
		this.removeNextTick = tag.contains("RemoveNextTick");
	}

	@Override
	public CompoundTag addExtraSyncData() {
		CompoundTag tag = new CompoundTag();
		if (this.orientation != null)
			tag.put("Orientation", this.newDoubleList(this.orientation.x, this.orientation.y, this.orientation.z));
		return tag;
	}

	@Override
	public void readExtraSyncData(CompoundTag tag) {
		if (tag.contains("Orientation", Tag.TAG_LIST)) {
			ListTag nextMotion = tag.getList("Orientation", Tag.TAG_DOUBLE);
			this.orientation = nextMotion.size() == 3 ? new Vec3(nextMotion.getDouble(0), nextMotion.getDouble(1), nextMotion.getDouble(2)) : null;
		} else {
			this.orientation = this.getDeltaMovement();
		}
	}

	public void setProjectileMass(float power) {
		this.entityData.set(PROJECTILE_MASS, power);
	}

	public float getProjectileMass() {
		return this.entityData.get(PROJECTILE_MASS);
	}

	public static void build(EntityType.Builder<? extends AbstractCannonProjectile> builder) {
		builder.clientTrackingRange(16)
				.updateInterval(1)
				.fireImmune()
				.sized(0.8f, 0.8f);
	}

	@Override
	protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5f;
	}

	protected double getGravity() {
		return this.isNoGravity() ? 0 : this.getBallisticProperties().gravity() * DimensionMunitionPropertiesHandler
			.getProperties(this.level).gravityMultiplier();
	}

	protected double getDragForce() {
		BallisticPropertiesComponent properties = this.getBallisticProperties();
		double vel = this.getDeltaMovement().length();
		double formDrag = properties.drag();
		double density = DimensionMunitionPropertiesHandler.getProperties(this.level).dragMultiplier();
		FluidState fluidState = this.level.getFluidState(this.blockPosition());
		if (!fluidState.isEmpty())
			density += FluidDragHandler.getFluidDrag(fluidState);
		double drag = formDrag * density * vel;
		if (properties.isQuadraticDrag())
			drag *= vel;
		return Math.min(drag, vel);
	}

	@Nonnull
	protected abstract BallisticPropertiesComponent getBallisticProperties();

	public void setChargePower(float power) {}

	@Override public boolean canHitEntity(Entity entity) { return super.canHitEntity(entity) && !(entity instanceof Projectile); }

	public boolean canLingerInGround() { return false; }

	public record ImpactResult(KinematicOutcome kinematics, boolean shouldRemove) {
		public enum KinematicOutcome { PENETRATE, STOP, BOUNCE }
	}

	public DamageSource indirectArtilleryFire() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".cannon_projectile", this, null, false).setScalesWithDifficulty().setExplosion();
	}

}
