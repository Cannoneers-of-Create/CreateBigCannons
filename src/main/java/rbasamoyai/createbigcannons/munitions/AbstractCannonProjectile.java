package rbasamoyai.createbigcannons.munitions;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions.GriefState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;
import rbasamoyai.ritchiesprojectilelib.network.ClientboundPreciseMotionSyncPacket;

public abstract class AbstractCannonProjectile extends Projectile {

	protected static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> PROJECTILE_MASS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.FLOAT);
	protected int inGroundTime = 0;
	@Nullable protected Vec3 inGroundPos = null;
	protected float damage;
	protected int inFluidTime = 0;
	protected int penetrationTime = 0;
	@Nullable protected Vec3 nextVelocity = null;
	@Nullable protected Vec3 orientation = null;
	protected BlockState lastPenetratedBlock = Blocks.AIR.defaultBlockState();
	protected boolean removeNextTick = false;
	protected int localSoundCooldown;
	protected WeakHashMap<Entity, Integer> untouchableEntities = new WeakHashMap<>();

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
		if (this.level().isClientSide || this.level().hasChunk(cpos.x, cpos.z)) {
			super.tick();

			if (this.nextVelocity != null) {
				boolean stop = this.nextVelocity.lengthSqr() < 1e-4d;
				// Bouncing is stochastic
				if (!this.level().isClientSide || stop) {
					if (stop) {
						this.setInGround(true);
						this.setGroundPos(this.position());
					}
					this.setDeltaMovement(this.nextVelocity);
				}
				this.nextVelocity = null;
			}

			if (!this.isInGround())
				this.clipAndDamage();

			this.onTickRotate();

			if (this.isInGround()) {
				this.setDeltaMovement(Vec3.ZERO);
				if (!this.level().isClientSide) {
					if (this.shouldFall()) {
						this.setInGround(false);
						this.setGroundPos(null);
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

			for (Iterator<Map.Entry<Entity, Integer>> iter = this.untouchableEntities.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<Entity, Integer> entry = iter.next();
				if (entry.getKey().isRemoved() || entry.getValue() > 0 && entry.getValue() - 1 == 0) {
					iter.remove();
				} else if (entry.getValue() > 0) {
					entry.setValue(entry.getValue() - 1);
				}
			}

			if (this.inFluidTime > 0)
				--this.inFluidTime;
			if (this.penetrationTime > 0)
				--this.penetrationTime;
			if (this.localSoundCooldown > 0)
				--this.localSoundCooldown;

			if (this.level() instanceof ServerLevel slevel && !this.isRemoved()) {
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

	public void setOrientation(Vec3 orientation) { this.orientation = orientation; }

	@Override
	public void lerpTo(double x, double y, double z, float yRot, float xRot, int lerpSteps, boolean teleport) {
		if (this.tickCount < 2)
			return;
		super.lerpTo(x, y, z, yRot, xRot, lerpSteps, teleport);
	}

	/**
	 * Utility control for local sound cooldown, e.g. flyby sounds.
	 *
	 * @param value 0 to allow flyby sounds
	 *              <br>Less than 0 to mark as running local sounds
	 *              <br>Greater than 0 to stop playing local sounds
	 */
	public void setLocalSoundCooldown(int value) { this.localSoundCooldown = value; }
	public int getLocalSoundCooldown() { return this.localSoundCooldown; }

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
			BlockHitResult blockResult = this.level().clip(new ClipContext(currentStart, currentEnd, ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE, this));
			if (blockResult.getType() != HitResult.Type.MISS)
				currentEnd = blockResult.getLocation();
			if (p == 0) {
				BlockHitResult fluidResult = this.level().clip(new ClipContext(currentStart, currentEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));
				if (fluidResult.getType() != HitResult.Type.MISS) {
					BlockPos fluidPos = fluidResult.getBlockPos();
					BlockState state = this.level().getBlockState(fluidPos);
					if (state.getBlock() instanceof LiquidBlock) {
						if (this.inFluidTime <= 0) {
							stop = this.onImpactFluid(projCtx, state, this.level().getFluidState(fluidPos), fluidResult.getLocation(), fluidResult);
							if (stop)
								currentEnd = blockResult.getLocation();
						}
						this.inFluidTime = 2;
					}
				}
			}

			if (this.onClip(projCtx, currentStart, currentEnd)) {
				shouldRemove = true;
				break;
			}

			Vec3 disp = currentEnd.subtract(currentStart);
			AABB currentMovementRegion = this.getBoundingBox().expandTowards(disp).inflate(1).move(currentStart.subtract(pos));

			Vec3 endStart = currentStart;
			Vec3 endCopy = currentEnd;
			AABB thisBB = this.getBoundingBox();
			for (Entity target : this.level().getEntities(this, currentMovementRegion)) {
				if (projCtx.hasHitEntity(target) || !this.canHitEntity(target))
					continue;
				AABB targetBB = target.getBoundingBox();
				if (targetBB.intersects(thisBB) || targetBB.inflate(reach).clip(endStart, endCopy).isPresent())
					projCtx.addEntity(target);
			}
			if (stop)
				break;

			currentStart = currentEnd;
			t -= disp.length() * vmRecip;
			if (blockResult.getType() != HitResult.Type.MISS) {
				BlockPos bpos = blockResult.getBlockPos().immutable();
				BlockState state = this.level().getChunkAt(bpos).getBlockState(bpos);

				ImpactResult result = this.calculateBlockPenetration(projCtx, state, blockResult);
				switch (result.kinematics) {
					case PENETRATE -> {
						this.lastPenetratedBlock = state;
						this.penetrationTime = 2;
					}
					case BOUNCE -> {
						this.setDeltaMovement(trajectory.scale(1 - t));
						Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), blockResult);
						double elasticity = 1.7f;
						this.nextVelocity = trajectory.subtract(normal.scale(normal.dot(trajectory) * elasticity));
						this.setLocalSoundCooldown(3);
						stop = true;
					}
					case STOP -> {
						this.setDeltaMovement(trajectory.scale(1 - t));
						this.nextVelocity = Vec3.ZERO;
						this.lastPenetratedBlock = state;
						this.penetrationTime = 2;
						this.setLocalSoundCooldown(3);
						stop = true;
					}
				}
				shouldRemove |= result.shouldRemove;
			}
			if (shouldRemove || stop || t <= 0)
				break;
		}

		for (Entity e : projCtx.hitEntities())
			shouldRemove |= this.onHitEntity(e, projCtx);

		if (!this.level().isClientSide) {
			if (projCtx.griefState() != GriefState.NO_DAMAGE) {
				Vec3 oldVel = this.getDeltaMovement();
				for (Map.Entry<BlockPos, Float> queued : projCtx.getQueuedExplosions().entrySet()) {
					Vec3 impactPos = Vec3.atCenterOf(queued.getKey());
					ImpactExplosion explosion = new ImpactExplosion(this.level(), this, this.indirectArtilleryFire(false),
						impactPos.x, impactPos.y, impactPos.z, queued.getValue(), Level.ExplosionInteraction.BLOCK);
					CreateBigCannons.handleCustomExplosion(this.level(), explosion);
				}
				this.setDeltaMovement(oldVel);
			}
			for (ClientboundPlayBlockHitEffectPacket pkt : projCtx.getPlayedEffects())
				NetworkPlatform.sendToClientTracking(pkt, this);
		}
		if (!this.level().isClientSide || !stop)
			this.orientation = trajectory;
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

	/**
	 * Used for calculating projectile skipping and effects.
	 *
	 * @param fluidHitResult
	 * @return if projectile clipping should only go up to on impact with the fluid.
	 */
	protected boolean onImpactFluid(ProjectileContext projectileContext, BlockState blockState, FluidState fluidState,
									Vec3 impactPos, BlockHitResult fluidHitResult) {
		Vec3 pos = this.position();
		Vec3 accel = this.getForces(this.position(), this.getDeltaMovement());
		Vec3 curVel = this.getDeltaMovement().add(accel);

		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), fluidHitResult);
		double incidence = Math.max(0, curVel.normalize().dot(normal.reverse()));
		double velMag = curVel.length();
		double mass = this.getProjectileMass();
		double incidentVel = velMag * incidence;
		double momentum = mass * incidentVel;

		double projectileDeflection = this.getBallisticProperties().deflection();
		double fluidDensity = FluidDragHandler.getFluidDrag(fluidState);

		boolean canBounce = CBCConfigs.SERVER.munitions.projectilesCanBounce.get();
		double baseChance = CBCConfigs.SERVER.munitions.baseProjectileFluidBounceChance.getF();
		boolean criticalAngle = projectileDeflection > 1e-2d && incidence <= projectileDeflection;
		boolean buoyant = fluidDensity > 1e-2d && momentum < fluidDensity;

		double incidenceFactor = criticalAngle ? Math.max(0, 1 - incidence / projectileDeflection) : 0;
		double massFactor = buoyant ? 0 : Math.max(0, 1 - momentum / fluidDensity);
		double chance = Math.max(baseChance, incidenceFactor * massFactor);

		boolean bounced = canBounce && criticalAngle && buoyant && this.level().getRandom().nextDouble() < chance;
		if (bounced) {
			this.setDeltaMovement(fluidHitResult.getLocation().subtract(pos));
			double elasticity = 1.7f;
			this.nextVelocity = curVel.subtract(normal.scale(normal.dot(curVel) * elasticity));
		}
		if (!this.level().isClientSide) {
			Vec3 effectNormal = bounced ? normal.scale(incidentVel) : curVel.reverse();
			Vec3 fluidExplosionPos = fluidHitResult.getLocation();
			projectileContext.addPlayedEffect(new ClientboundPlayBlockHitEffectPacket(blockState, this.getType(), bounced, true,
				fluidExplosionPos.x, fluidExplosionPos.y, fluidExplosionPos.z, (float) effectNormal.x,
				(float) effectNormal.y, (float) effectNormal.z));
		}
		return bounced;
	}

	protected boolean onHitEntity(Entity entity, ProjectileContext projectileContext) {
		if (this.getProjectileMass() <= 0)
			return false;
		if (!this.level().isClientSide) {
			EntityDamagePropertiesComponent properties = this.getDamageProperties();
			entity.setDeltaMovement(this.getDeltaMovement().scale(this.getKnockback(entity)));
			DamageSource source = this.getEntityDamage(entity);

			if (properties == null || properties.ignoresInvulnerability()) entity.invulnerableTime = 0;
			entity.hurt(source, this.damage);
			if (properties == null || !properties.rendersInvulnerable()) entity.invulnerableTime = 0;

			float penalty = entity.isAlive() ? 2f : 0.2f;
			this.setProjectileMass(Math.max(this.getProjectileMass() - penalty, 0));
		}
		return this.onImpact(new EntityHitResult(entity), new ImpactResult(ImpactResult.KinematicOutcome.PENETRATE, false), projectileContext);
	}

	protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
		return false;
	}

	protected DamageSource getEntityDamage(Entity entity) {
		return this.indirectArtilleryFire(this.getDamageProperties().ignoresEntityArmor());
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
		super.setOnGround(inGround);
	}

	public boolean isInGround() {
		return (this.entityData.get(ID_FLAGS) & 1) != 0;
	}

	public void setGroundPos(@Nullable Vec3 groundPos) { this.inGroundPos = groundPos; }
	@Nullable public Vec3 getGroundPos() { return this.inGroundPos; }

	@Override
	public void setOnGround(boolean onGround) {
		this.setInGround(onGround);
	}

	private boolean shouldFall() {
		return this.isInGround() && this.inGroundPos != null && this.level().noCollision(new AABB(this.inGroundPos, this.inGroundPos).inflate(0.06d));
	}

	public void updateKinematics(ClientboundPreciseMotionSyncPacket packet) {
		if (this.getDeltaMovement().lengthSqr() > 1e-4d)
			this.orientation = this.getDeltaMovement();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("ProjectileMass", this.getProjectileMass());
		tag.putBoolean("InGround", this.isInGround());
		if (this.inGroundPos != null)
			tag.put("InGroundPos", this.newDoubleList(this.inGroundPos.x, this.inGroundPos.y, this.inGroundPos.z));
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
			ListTag orientationTag = tag.getList("Orientation", Tag.TAG_DOUBLE);
			this.orientation = orientationTag.size() == 3 ? new Vec3(orientationTag.getDouble(0), orientationTag.getDouble(1), orientationTag.getDouble(2)) : null;
		} else {
			this.orientation = this.getDeltaMovement();
		}
		if (tag.contains("InGroundPos", Tag.TAG_LIST)) {
			ListTag posTag = tag.getList("InGroundPos", Tag.TAG_DOUBLE);
			this.inGroundPos = posTag.size() == 3 ? new Vec3(posTag.getDouble(0), posTag.getDouble(1), posTag.getDouble(2)) : null;
		} else {
			this.inGroundPos = null;
		}
		this.lastPenetratedBlock = tag.contains("LastPenetration", Tag.TAG_COMPOUND)
			? NbtUtils.readBlockState(this.level().holderLookup(CBCRegistryUtils.getBlockRegistryKey()), tag.getCompound("LastPenetration"))
			: Blocks.AIR.defaultBlockState();
		this.removeNextTick = tag.contains("RemoveNextTick");
	}

	public void baseWriteSpawnData(FriendlyByteBuf buf) {
		Vec3 vel = this.getDeltaMovement();
		Vec3 orientation = this.orientation == null ? vel : this.orientation;
		buf.writeFloat(this.getXRot())
			.writeFloat(this.getYRot())
			.writeDouble(vel.x)
			.writeDouble(vel.y)
			.writeDouble(vel.z)
			.writeDouble(orientation.x)
			.writeDouble(orientation.y)
			.writeDouble(orientation.z);
	}

	public void baseReadSpawnData(FriendlyByteBuf buf) {
		this.setXRot(buf.readFloat());
		this.setYRot(buf.readFloat());
		this.setDeltaMovement(buf.readDouble(), buf.readDouble(), buf.readDouble());
		this.orientation = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
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
		return 0;
	}

	@Override
	protected AABB makeBoundingBox() {
		AABB box = super.makeBoundingBox();
		return box.move(0, -box.getYsize() * 0.5d, 0);
	}

	@Override
	public Vec3 getLightProbePosition(float partialTicks) {
		Vec3 eyePos = super.getLightProbePosition(partialTicks);
		return this.isInGround() && this.orientation != null ? eyePos.subtract(this.orientation.normalize().scale(0.1)) : eyePos;
	}

	protected double getGravity() {
		return this.isNoGravity() ? 0 : this.getBallisticProperties().gravity() * DimensionMunitionPropertiesHandler
			.getProperties(this.level()).gravityMultiplier();
	}

	protected double getDragForce() {
		BallisticPropertiesComponent properties = this.getBallisticProperties();
		double vel = this.getDeltaMovement().length();
		double formDrag = properties.drag();
		double density = DimensionMunitionPropertiesHandler.getProperties(this.level()).dragMultiplier();
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
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

	@Override
	public boolean canHitEntity(Entity entity) {
		if (!super.canHitEntity(entity))
			return false;
		if (entity instanceof Projectile)
			return false; // TODO better detection for interception?
		return !this.untouchableEntities.containsKey(entity);
	}

	public void addUntouchableEntity(Entity entity, int duration) {
		if (entity.isRemoved())
			return;
		if (duration < 1)
			throw new IllegalArgumentException("Use #addAlwaysUntouchableEntity when duration < 1 (was " + duration + ")");
		this.untouchableEntities.put(entity, duration);
	}

	public void addAlwaysUntouchableEntity(Entity entity) { this.untouchableEntities.put(entity, -1); }
	public void removeUntouchableEntity(Entity entity) { this.untouchableEntities.remove(entity); }

	public boolean canLingerInGround() { return false; }

	public record ImpactResult(KinematicOutcome kinematics, boolean shouldRemove) {
		public enum KinematicOutcome { PENETRATE, STOP, BOUNCE }
	}

	public DamageSource indirectArtilleryFire(boolean bypassArmor) {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.CANNON_PROJECTILE), bypassArmor);
	}

}
