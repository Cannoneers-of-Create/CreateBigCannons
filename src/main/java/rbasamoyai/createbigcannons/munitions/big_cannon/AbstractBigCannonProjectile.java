package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public abstract class AbstractBigCannonProjectile extends AbstractCannonProjectile {

	protected AbstractBigCannonProjectile(EntityType<? extends AbstractCannonProjectile> type, Level level) { super(type, level); }

	public abstract BlockState getRenderedBlockState();

	@Nullable @Override protected ParticleOptions getTrailParticles() { return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE; }

}
