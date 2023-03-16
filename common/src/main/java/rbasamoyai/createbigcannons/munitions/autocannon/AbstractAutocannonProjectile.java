package rbasamoyai.createbigcannons.munitions.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

public abstract class AbstractAutocannonProjectile extends AbstractCannonProjectile {

	protected int ageRemaining;

	protected AbstractAutocannonProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
		super(type, level);
		this.ageRemaining = 60;
		this.damage = 15;
	}

	@Override protected float getKnockback(Entity target) { return 0.5f; }
	@Override protected double overPenetrationPower(double hardness, double curPom) { return 0; }

	@Override
	public void tick() {
		super.tick();

		if (!this.level.isClientSide && this.level.hasChunkAt(this.blockPosition())) {
			this.ageRemaining--;
			if (this.ageRemaining <= 0) this.expireProjectile();
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

	@Override
	protected void onDestroyBlock(BlockState state, BlockHitResult result) {
		if (this.level instanceof ServerLevel) {
			BlockPos pos = result.getBlockPos();
			if (state.getDestroySpeed(this.level, pos) == -1) return;

			Vec3 curVel = this.getDeltaMovement();
			double curPom = this.getProjectileMass() * curVel.length();
			double hardness = BlockHardnessHandler.getHardness(state) * 10;
			CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos.immutable(), (int) Math.min(curPom, hardness), state, this.level);

			if (curPom > hardness) {
				double startMass = this.getProjectileMass();
				this.setDeltaMovement(curVel.normalize().scale(Math.max(curPom - hardness, 0) / startMass));
			} else {
				this.onFinalImpact(result);
				this.discard();
			}
		}
	}

	@Override
	protected void onFinalImpact(HitResult result) {
		super.onFinalImpact(result);
		if (!this.isRemoved()) this.discard();
	}

	public boolean isTracer() { return (this.entityData.get(ID_FLAGS) & 2) != 0; }

	@Override protected float getGravity() { return 0; }
	@Override protected float getDrag() { return 1; }

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
	
}
