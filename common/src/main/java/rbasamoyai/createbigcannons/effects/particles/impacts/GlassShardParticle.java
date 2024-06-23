package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class GlassShardParticle extends SplinterParticle {

	GlassShardParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
	}

	public static class Provider implements ParticleProvider<GlassShardParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) { this.sprites = sprites; }

		@Override
		public Particle createParticle(GlassShardParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = type.state();
			if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON))
				return null;
			GlassShardParticle particle = new GlassShardParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate);
			IndexPlatform.updateSprite(particle, blockstate, new BlockPos(x, y, z));
			particle.setSecondarySprite(this.sprites.get(level.getRandom()));
			return particle;
		}
	}

}
