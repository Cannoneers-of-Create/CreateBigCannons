package rbasamoyai.createbigcannons.munitions;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions.GriefState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

public abstract class AbstractCannonProjectile<T extends BaseProjectileProperties> extends Projectile implements PropertiesMunitionEntity<T> {

	protected static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> PROJECTILE_MASS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.FLOAT);
	protected int inGroundTime = 0;
	protected float damage;

	protected AbstractCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) {
		super(type, level);
		T properties = this.getProperties();
		this.damage = properties == null ? 0 : properties.entityDamage();
		this.setProjectileMass(properties == null ? 0 : properties.durabilityMass());
	}

	@Override
	public void tick() {
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			super.tick();

			if (!this.isInGround()) this.clipAndDamage();

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
				Vec3 accel = oldVel.normalize().scale(-this.getDragForce())
					.add(0.0d, this.getGravity(), 0.0d);
				Vec3 newPos = oldPos.add(oldVel).add(accel.scale(0.5));
				this.setPos(newPos);
				this.setDeltaMovement(oldVel.add(accel));

				this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
				this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
			}

			if (this.level instanceof ServerLevel slevel && !this.isRemoved()) {
				if (CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
					ChunkPos cpos1 = new ChunkPos(this.blockPosition());
					RitchiesProjectileLib.queueForceLoad(slevel, cpos1.x, cpos1.z);
				}
			}
		}
	}

	protected void onTickRotate() {}

	protected void clipAndDamage() {
		ProjectileContext projCtx = new ProjectileContext(this, CBCConfigs.SERVER.munitions.damageRestriction.get());

		Vec3 pos = this.position();
		Vec3 start = pos;
		double reach = Math.max(this.getBbWidth(), this.getBbHeight()) * 0.5;

		double t = 1;
		int MAX_ITER = 100;
		for (int p = 0; p < MAX_ITER; ++p) {
			boolean breakEarly = false;
			Vec3 vel = this.getDeltaMovement().scale(t);
			if (vel.lengthSqr() < 1e-4d) break;

			Vec3 end = start.add(vel);
			BlockHitResult bResult = this.level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
			if (bResult.getType() != HitResult.Type.MISS) end = bResult.getLocation();

			AABB currentMovementRegion = this.getBoundingBox().expandTowards(end.subtract(start)).inflate(1).move(start.subtract(pos));

			Vec3 finalStart = start;
			Vec3 finalEnd = end;
			AABB thisBB = this.getBoundingBox();
			for (Entity target : this.level.getEntities(this, currentMovementRegion, e -> {
				if (projCtx.hasHitEntity(e) || !this.canHitEntity(e)) return false;
				AABB bb = e.getBoundingBox();
				return bb.intersects(thisBB) || bb.inflate(reach).clip(finalStart, finalEnd).isPresent();
			})) {
				projCtx.addEntity(target);
			}

			Vec3 hitLoc = end;
			if (bResult.getType() != HitResult.Type.MISS) {
				BlockPos bpos = bResult.getBlockPos().immutable();
				BlockState state = this.level.getChunkAt(bpos).getBlockState(bpos);

				Vec3 curVel = this.getDeltaMovement();
				double mag = curVel.length();
				boolean flag1 = projCtx.getLastState().isAir();
				if (!flag1 || !this.tryBounceOffBlock(state, bResult)) {
					projCtx.setLastState(state);

					double startMass = this.getProjectileMass();
					double curPom = startMass * mag;
					double hardness = BlockArmorPropertiesHandler.getProperties(state).hardness(this.level, state, bpos, true);

					if (projCtx.griefState() == GriefState.NO_DAMAGE || state.getDestroySpeed(this.level, bpos) == -1 || curPom < hardness) {
						this.setInGround(true);
						this.setDeltaMovement(Vec3.ZERO);
						this.onImpact(bResult, true);
						breakEarly = true;
					} else {
						state.onProjectileHit(this.level, state, bResult, this);
						this.onDestroyBlock(state, bResult);
						if (this.isRemoved()) {
							breakEarly = true;
						} else {
							this.onImpact(bResult, false);
							if (this.isRemoved()) {
								breakEarly = true;
							} else {
								double f = this.overPenetrationPower(hardness, curPom);
								if (flag1 && f > 0) {
									projCtx.queueExplosion(bpos, (float) f);
								}
							}
						}
					}
				}
			}
			Vec3 disp = hitLoc.subtract(start);
			start = hitLoc;
			if (this.onClip(projCtx, start)) break;
			if (breakEarly) break;
			t -= disp.length() / vel.length();
			if (t < 0) break;
		}

		for (Entity e : projCtx.hitEntities()) this.onHitEntity(e);

		if (!this.level.isClientSide && projCtx.griefState() != GriefState.NO_DAMAGE) {
			Vec3 oldVel = this.getDeltaMovement();
			for (Map.Entry<BlockPos, Float> explosion : projCtx.getQueuedExplosions().entrySet()) {
				BlockPos bpos = explosion.getKey();
				this.level.explode(this, bpos.getX() + 0.5, bpos.getY() + 0.5, bpos.getZ() + 0.5, explosion.getValue(), Explosion.BlockInteraction.DESTROY);
			}
			this.setDeltaMovement(oldVel);
		}
	}

	/**
	 * @param ctx
	 * @param pos
	 * @return true to stop iterating, false otherwise
	 */
	protected boolean onClip(ProjectileContext ctx, Vec3 pos) {
		return false;
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
			if (momentum > BlockArmorPropertiesHandler.getProperties(state).hardness(this.level, state, result.getBlockPos(), true) * 0.5) {
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

	/** Use for fuzes and any other effects */
	protected void onImpact(HitResult result, boolean stopped) {
		if (result.getType() == HitResult.Type.BLOCK) {
			BlockState state = this.level.getBlockState(((BlockHitResult) result).getBlockPos());
			state.onProjectileHit(this.level, state, (BlockHitResult) result, this);
		}
	}

	protected abstract void onDestroyBlock(BlockState state, BlockHitResult result);

	protected void onHitEntity(Entity entity) {
		if (this.getProjectileMass() <= 0) return;
		if (!this.level.isClientSide) {
			T properties = this.getProperties();
			entity.setDeltaMovement(this.getDeltaMovement().scale(this.getKnockback(entity)));
			DamageSource source = this.getEntityDamage();

			if (properties == null || properties.ignoresEntityArmor()) entity.invulnerableTime = 0;
			entity.hurt(source, this.damage);
			if (properties == null || !properties.rendersInvulnerable()) entity.invulnerableTime = 0;

			double penalty = entity.isAlive() ? 2 : 0.2;
			this.setProjectileMass((float) Math.max(this.getProjectileMass() - penalty, 0));
			this.onImpact(new EntityHitResult(entity), this.getProjectileMass() <= 0);
		}
	}

	protected DamageSource getEntityDamage() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".cannon_projectile", this, null);
	}

	protected float getKnockback(Entity target) {
		T properties = this.getProperties();
		return properties == null ? 0 : properties.knockback();
	}

	protected boolean canDeflect(BlockHitResult result) { return false; }
	protected boolean canBounceOffOf(BlockState state) { return isBounceableOffOf(state); }

	public static boolean isDeflector(BlockState state) {
		if (state.is(CBCTags.CBCBlockTags.DEFLECTS_SHOTS)) return true;
		if (state.is(CBCTags.CBCBlockTags.DOESNT_DEFLECT_SHOTS)) return false;
		Material material = state.getMaterial();
		return material == Material.METAL || material == Material.HEAVY_METAL;
	}

	public static boolean isBounceableOffOf(BlockState state) {
		if (state.is(CBCTags.CBCBlockTags.DOESNT_BOUNCE_SHOTS)) return false;
		if (state.is(CBCTags.CBCBlockTags.BOUNCES_SHOTS)) return true;
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
		ListTag motionTag = tag.getList("Motion", Tag.TAG_DOUBLE);
		this.setDeltaMovement(motionTag.getDouble(0), motionTag.getDouble(1), motionTag.getDouble(2));
	}

	public void setProjectileMass(float power) {
		this.entityData.set(PROJECTILE_MASS, power);
	}

	public float getProjectileMass() {
		return this.entityData.get(PROJECTILE_MASS);
	}

	public static void build(EntityType.Builder<? extends AbstractCannonProjectile<?>> builder) {
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
		if (this.isNoGravity())
			return 0;
		T properties = this.getProperties();
		double val = properties == null ? -0.05d : properties.gravity();
		double multiplier = DimensionMunitionPropertiesHandler.getProperties(this.level).gravityMultiplier();
		return val * multiplier;
	}

	protected double getDragForce() {
		T properties = this.getProperties();
		if (properties == null)
			return 0;
		double magnitude = this.getDeltaMovement().length();
		double drag = properties.drag() * DimensionMunitionPropertiesHandler.getProperties(this.level).dragMultiplier() * magnitude;
		if (properties.isQuadraticDrag())
			drag *= magnitude;
		return Math.min(drag, magnitude);
	}

	public void setChargePower(float power) {}

	@Override public boolean canHitEntity(Entity entity) { return super.canHitEntity(entity) && !(entity instanceof Projectile); }

	public boolean canLingerInGround() { return false; }

	public enum BounceType {
		DEFLECT,
		RICOCHET,
		NO_BOUNCE
	}

	public DamageSource indirectArtilleryFire() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".cannon_projectile", this, null).setScalesWithDifficulty().setExplosion();
	}

}
