package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class LeafParticle extends SplinterParticle {

	LeafParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
		this.gravity = 0.5f;
		this.friction = 0.9f;
		this.pitch = this.random.nextFloat() * Mth.HALF_PI;
	}

	public static class Provider implements ParticleProvider<LeafParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) { this.sprites = sprites; }

		@Override
		public Particle createParticle(LeafParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = type.state();
			if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON))
				return null;
			LeafParticle particle = new LeafParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate);
			IndexPlatform.updateSprite(particle, blockstate, new BlockPos(x, y, z));
			particle.setSecondarySprite(this.sprites.get(level.getRandom()));
			return particle;
		}
	}

}
