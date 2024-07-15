package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CBCBlockParticle extends TerrainParticle {

	CBCBlockParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;
	}

	public static class Provider implements ParticleProvider<CBCBlockParticleData> {
		@Override
		public Particle createParticle(CBCBlockParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = type.state();
			if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON))
				return null;
			CBCBlockParticle particle = new CBCBlockParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate);
			IndexPlatform.updateSprite(particle, blockstate, new BlockPos(x, y, z));
			return particle;
		}
	}

}
