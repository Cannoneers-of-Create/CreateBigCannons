package rbasamoyai.createbigcannons.munitions.autocannon;

import com.mojang.math.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractAutocannonProjectile extends AbstractCannonProjectile {

	protected double displacement = 0;
	protected int ageRemaining;

	protected AbstractAutocannonProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void onTickRotate() {
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();

		if (!this.isInGround()) {
			Vec3 vel = this.getDeltaMovement();
			if (vel.lengthSqr() > 0.005d) {
				this.setYRot((float) (Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
				this.setXRot((float) (Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
			}

			this.setYRot(this.getYRot());
			this.setXRot(this.getXRot());
		}
	}

	@Override protected double overPenetrationPower(double hardness, double curPom) { return 0; }

	@Override
	public void tick() {
		Vec3 prevPos = this.position();
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level.isClientSide || this.level.hasChunk(cpos.x, cpos.z)) {
			super.tick();
			this.displacement += this.position().distanceTo(prevPos);
			if (!this.level.isClientSide) {
				this.ageRemaining--;
				if (this.ageRemaining <= 0)
					this.expireProjectile();
			}
			if (!this.isInGround()) {
				TrailType trailType = CBCConfigs.SERVER.munitions.autocannonTrailType.get();
				if (trailType != TrailType.NONE) {
					int lifetime = trailType == TrailType.SHORT ? 50 : 100 + this.level.random.nextInt(50);
					for (int i = 0; i < 10; ++i) {
						double partial = i * 0.1f;
						double dx = Mth.lerp(partial, this.xOld, this.getX());
						double dy = Mth.lerp(partial, this.yOld, this.getY());
						double dz = Mth.lerp(partial, this.zOld, this.getZ());
						double sx = this.level.random.nextDouble(-0.002d, 0.002d);
						double sy = this.level.random.nextDouble(-0.002d, 0.002d);
						double sz = this.level.random.nextDouble(-0.002d, 0.002d);
						this.level.addAlwaysVisibleParticle(new TrailSmokeParticleData(lifetime), true, dx, dy, dz, sx, sy, sz);
					}
				}
			}
		}
	}

	protected void expireProjectile() {
		this.discard();
	}

	public void setTracer(boolean tracer) {
		if (tracer) {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) | 2));
		} else {
			this.entityData.set(ID_FLAGS, (byte)(this.entityData.get(ID_FLAGS) & 0b11111101));
		}
	}

	public void setLifetime(int lifetime) { this.ageRemaining = lifetime; }

	@Override
	protected void onDestroyBlock(BlockState state, BlockHitResult result) {
		if (this.level instanceof ServerLevel) {
			BlockPos pos = result.getBlockPos();
			if (state.getDestroySpeed(this.level, pos) == -1) return;

			Vec3 curVel = this.getDeltaMovement();
			double curPom = this.getProjectileMass() * curVel.length();
			double hardness = BlockArmorPropertiesHandler.getProperties(state).hardness(this.level, state, pos, true) * 10;
			CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos.immutable(), (int) Math.min(curPom, hardness), state, this.level);

			if (curPom > hardness) {
				double startMass = this.getProjectileMass();
				this.setDeltaMovement(curVel.normalize().scale(Math.max(curPom - hardness, 0) / startMass));
			} else {
				this.onImpact(result, false);
				this.discard();
			}
		}
	}

	@Override
	protected void onImpact(HitResult result, boolean stopped) {
		super.onImpact(result, stopped);
		if (stopped && !this.isRemoved()) this.discard();
	}

	public boolean isTracer() { return (this.entityData.get(ID_FLAGS) & 2) != 0; }

	public double getTotalDisplacement() { return this.displacement; }
	public void setTotalDisplacement(double value) { this.displacement = value; }

    @Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Tracer", this.isTracer());
		tag.putInt("Age", this.ageRemaining);
		tag.putDouble("Displacement", this.displacement);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.getBoolean("Tracer"));
		this.ageRemaining = tag.getInt("Age");
		this.displacement = tag.getFloat("Displacement");
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return super.canDeflect(result) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.autocannonDeflectChance.getF();
	}

	@Override
	protected boolean canBounceOffOf(BlockState state) {
		return super.canBounceOffOf(state) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.autocannonDeflectChance.getF();
	}

	public AutocannonAmmoType getAutocannonRoundType() { return AutocannonAmmoType.AUTOCANNON; }

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
