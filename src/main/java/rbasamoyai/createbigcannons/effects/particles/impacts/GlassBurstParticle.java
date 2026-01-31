package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;

public class GlassBurstParticle extends SplinterBurstParticle {

	GlassBurstParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, BlockState blockState, int count) {
		super(level, x, y, z, dx, dy, dz, blockState, count);
	}

	public ParticleOptions getSubParticle() {
		return new GlassShardParticleData(this.blockState);
	}

	public static class Provider implements ParticleProvider<GlassBurstParticleData> {
		@Override
		public Particle createParticle(GlassBurstParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			GlassBurstParticle particle = new GlassBurstParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.blockState(), type.count());
			particle.setLifetime(1);
			return particle;
		}
	}

}
