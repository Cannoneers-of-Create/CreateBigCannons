package rbasamoyai.createbigcannons.munitions;

import com.mojang.math.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions.GriefState;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public abstract class AbstractCannonProjectile extends AbstractHurtingProjectile {

	protected static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> PENETRATION_POINTS = SynchedEntityData.defineId(AbstractCannonProjectile.class, EntityDataSerializers.FLOAT);
	protected int inGroundTime = 0;
	protected float damage = 50;
	
	protected AbstractCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
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
			if (!this.isNoGravity()) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0f, this.getGravity(), 0.0f));
			}
		}
		
		if (this.xRotO == 0 && this.yRotO == 0) {
			Vec3 vel = this.getDeltaMovement();
			this.setYRot((float)(Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
			this.setXRot((float)(Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
			
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
		}
		
		float oldXRot = this.xRotO;
		float oldYRot = this.yRotO;
		
		super.tick();
		
		this.xRotO = oldXRot;
		this.yRotO = oldYRot;
		
		if (!this.isInGround()) {
			Vec3 vel = this.getDeltaMovement();
			this.setYRot((float)(Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
			this.setXRot((float)(Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
			
			this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
			this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		}
		
		if (!this.isInGround()) {
			for (int i = 0; i < 10; ++i) {
				double partial = i * 0.1f;
				double dx = Mth.lerp(partial, this.xOld, this.getX());
				double dy = Mth.lerp(partial, this.yOld, this.getY());
				double dz = Mth.lerp(partial, this.zOld, this.getZ());
				this.level.addParticle(this.getTrailParticle(), dx, dy, dz, 0.0d, 0.0d, 0.0d);
			}
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			Entity entity = result.getEntity();
			if (entity instanceof Projectile) return;
			
			entity.setDeltaMovement(this.getDeltaMovement().scale(this.getKnockback(entity)));
			DamageSource source = DamageSource.thrown(this, null).bypassArmor();
			entity.hurt(source, this.damage);
			if (!CBCConfigs.SERVER.munitions.invulProjectileHurt.get()) result.getEntity().invulnerableTime = 0;
			
			if (result.getEntity().isAlive()) {
				this.setPenetrationPoints((byte) Math.max(0, this.getPenetrationPoints() - 2));
				if (this.getPenetrationPoints() == 0) {
					this.discard();
				}
			}
		}
	}

	protected float getKnockback(Entity target) { return 2.0f; }
	
	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			Vec3 hitLoc = result.getLocation();
			BlockPos pos = result.getBlockPos();
			BlockState oldState = this.level.getBlockState(pos);

			boolean flag = true;
			Vec3 oldVel = this.getDeltaMovement();
			Vec3 normal = new Vec3(result.getDirection().step());
			if (this.getPenetrationPoints() > 0) {
				if (this.canDeflect(result)) {
					// TODO: spall effect
					this.setPos(hitLoc);
					this.setDeltaMovement(oldVel.subtract(normal.scale(normal.dot(oldVel) * 1.9)));
					flag = false;
				} else if (this.canRicochet(result)) {
					this.setPos(hitLoc);
					this.setDeltaMovement(oldVel.subtract(normal.scale(normal.dot(oldVel) * 1.5)));
					flag = false;
				}
			}

			if (flag) {
				if (this.canBreakBlock(result)) {
					Vec3 currentVel = this.getDeltaMovement();
					this.level.destroyBlock(pos, false);
					this.level.explode(null, hitLoc.x, hitLoc.y, hitLoc.z, this.getPenetratingExplosionPower(), Explosion.BlockInteraction.DESTROY);
					this.setDeltaMovement(currentVel);
					this.setPenetrationPoints(result, oldState);
				} else {
					if (CBCConfigs.SERVER.munitions.damageRestriction.get() == GriefState.NO_DAMAGE) {
						this.level.explode(null, hitLoc.x, hitLoc.y, hitLoc.z, 2, Explosion.BlockInteraction.NONE);
					}
					this.setInGround(true);
					this.setDeltaMovement(Vec3.ZERO);
					this.setPos(hitLoc);
					this.setPenetrationPoints((byte) 0);
				}
			} else {
				this.setPenetrationPoints(Math.max(0, this.getPenetrationPoints() - oldState.getBlock().getExplosionResistance()));
			}
		}
	}

	protected boolean canBreakBlock(BlockHitResult result) {
		if (this.getPenetrationPoints() <= 0 || this.canDeflect(result)) return false;
		BlockPos pos = result.getBlockPos();
		BlockState remainingBlock = this.level.getBlockState(pos);
		return remainingBlock.getDestroySpeed(this.level, pos) != -1.0;
	}

	protected boolean canDeflect(BlockHitResult result) { return false; }

	public static boolean isDeflector(BlockState state) {
		if (state.is(CBCTags.BlockCBC.DEFLECTS_SHOTS)) return true;
		if (state.is(CBCTags.BlockCBC.DOESNT_DEFLECT_SHOTS)) return false;
		Material material = state.getMaterial();
		return material == Material.METAL || material == Material.HEAVY_METAL;
	}

	protected boolean canRicochet(BlockHitResult result) {
		Vec3 oldVel = this.getDeltaMovement();
		Vec3 normal = new Vec3(result.getDirection().step());
		double fc = normal.dot(oldVel) / oldVel.length();
		return -0.5 <= fc && fc <= 0; // cos 120 <= fc <= cos 90
	}

	protected void setPenetrationPoints(BlockHitResult result, BlockState hitBlock) {
		this.setPenetrationPoints(Math.max(0, this.getPenetrationPoints() - hitBlock.getBlock().getExplosionResistance()));
	}

	protected float getPenetratingExplosionPower() { return 2; }

	@Override public boolean hurt(DamageSource source, float damage) { return false; }

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ID_FLAGS, (byte) 0);
		this.entityData.define(PENETRATION_POINTS, 0.0f);
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
		tag.putFloat("Power", this.getPenetrationPoints());
		tag.putBoolean("InGround", this.isInGround());
		tag.putFloat("Damage", this.damage);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setPenetrationPoints(tag.getFloat("Power"));
		this.setInGround(tag.getBoolean("InGround"));
		this.damage = tag.getFloat("Damage");
	}
	
	public void setPenetrationPoints(float power) {
		this.entityData.set(PENETRATION_POINTS, power);
	}
	
	public float getPenetrationPoints() {
		return CBCConfigs.SERVER.munitions.damageRestriction.get() == GriefState.NO_DAMAGE ? 0 : this.entityData.get(PENETRATION_POINTS);
	}

	public static void build(EntityType.Builder<? extends AbstractCannonProjectile> builder) {
		builder.setTrackingRange(16)
				.setUpdateInterval(1)
				.setShouldReceiveVelocityUpdates(true)
				.fireImmune()
				.sized(0.8f, 0.8f);
	}
	
	@Override protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.5f;
	}
	@Override protected float getInertia() {
		return 0.99f;
	}
	protected float getGravity() { return -0.05f; }
	
	@Override protected ParticleOptions getTrailParticle() {
		return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
	}

	public void setChargePower(float power) {}

	@Override public boolean canHitEntity(Entity entity) { return super.canHitEntity(entity); }

}
