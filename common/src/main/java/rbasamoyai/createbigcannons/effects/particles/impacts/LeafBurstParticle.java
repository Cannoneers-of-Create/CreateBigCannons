package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;

public class LeafBurstParticle extends SplinterBurstParticle {

	LeafBurstParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, BlockState blockState, int count) {
		super(level, x, y, z, dx, dy, dz, blockState, count);
	}

	public ParticleOptions getSubParticle() {
		return new LeafParticleData(this.blockState);
	}

	public static class Provider implements ParticleProvider<LeafBurstParticleData> {
		@Override
		public Particle createParticle(LeafBurstParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			LeafBurstParticle particle = new LeafBurstParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.blockState(), type.count());
			particle.setLifetime(1);
			return particle;
		}
	}

}
