package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractBigCannonProjectile extends AbstractCannonProjectile {

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) { super(type, level); }

	public abstract BlockState getRenderedBlockState();

	@Nullable @Override protected ParticleOptions getTrailParticles() { return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE; }

	@Override
	protected void onDestroyBlock(BlockHitResult result) {
		double startMass = this.getProjectileMass();
		Vec3 curVel = this.getDeltaMovement();
		double mag = curVel.length();
		double curPom = startMass * mag;

		double hardness = getHardness(this.level.getBlockState(result.getBlockPos()));
		this.setProjectileMass((float) Math.max(startMass - hardness, 0));
		this.setDeltaMovement(curVel.normalize().scale(Math.max(curPom - hardness, 0) / startMass));

		if (!this.level.isClientSide) this.level.destroyBlock(result.getBlockPos(), false);
	}

}
