package rbasamoyai.createbigcannons.munitions.autocannon;

import javax.annotation.Nullable;

import com.mojang.math.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

public abstract class AbstractAutocannonProjectile<T extends BaseProjectileProperties> extends AbstractCannonProjectile<T> {

	protected int ageRemaining;
	@Nullable private Vec3 prevPos = null;

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
		this.prevPos = this.position();

		super.tick();

		if (!this.level.isClientSide && this.level.hasChunkAt(this.blockPosition())) {
			this.ageRemaining--;
			if (this.ageRemaining <= 0) this.expireProjectile();
		}
	}

	@Nullable public Vec3 getPreviousPos() { return this.prevPos; }

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
			double hardness = BlockHardnessHandler.getHardness(state, this.level, pos) * 10;
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

    @Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Tracer", this.isTracer());
		tag.putInt("Age", this.ageRemaining);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.getBoolean("Tracer"));
		this.ageRemaining = tag.getInt("Age");
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return super.canDeflect(result) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.autocannonDeflectChance.getF();
	}

	@Override
	protected boolean canBounceOffOf(BlockState state) {
		return super.canBounceOffOf(state) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.autocannonDeflectChance.getF();
	}

}
