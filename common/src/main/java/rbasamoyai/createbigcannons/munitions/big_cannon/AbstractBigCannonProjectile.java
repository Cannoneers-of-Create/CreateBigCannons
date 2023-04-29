package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.mojang.math.Constants;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

public abstract class AbstractBigCannonProjectile extends AbstractCannonProjectile {

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractBigCannonProjectile> type, Level level) { super(type, level); }

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

			this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
			this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		}
	}

	public abstract BlockState getRenderedBlockState();

	@Nullable @Override protected ParticleOptions getTrailParticles() { return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE; }

	@Override
	protected void onDestroyBlock(BlockState state, BlockHitResult result) {
		double startMass = this.getProjectileMass();
		Vec3 curVel = this.getDeltaMovement();
		double mag = curVel.length();
		double curPom = startMass * mag;

		double hardness = BlockHardnessHandler.getHardness(state);
		this.setProjectileMass((float) Math.max(startMass - hardness, 0));
		this.setDeltaMovement(curVel.normalize().scale(Math.max(curPom - hardness, 0) / startMass));

		CBCCommonEvents.onCannonBreakBlock(this.level, result.getBlockPos());
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return super.canDeflect(result) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.bigCannonDeflectChance.getF();
	}

	@Override
	protected boolean canBounceOffOf(BlockState state) {
		return super.canBounceOffOf(state) && this.random.nextFloat() < CBCConfigs.SERVER.munitions.bigCannonDeflectChance.getF();
	}

}
