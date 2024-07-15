package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;

public class SplinterBurstParticle extends NoRenderParticle {

	protected final BlockState blockState;
	private final int count;

	SplinterBurstParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, BlockState blockState, int count) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.blockState = blockState;
		this.count = count;
	}

	@Override
	public void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		int actualCount = switch (minecraft.options.particles().get()) {
            case ALL -> this.count;
            case DECREASED -> this.count / 2;
            case MINIMAL -> this.count / 4;
        };
		ParticleOptions options = this.getSubParticle();
		double scale = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd) * 0.75;
		scale = Math.min(scale, 2);
		for (int i = 0; i < actualCount; ++i) {
			double x = this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.75;
			double y = this.y + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.75;
			double z = this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.75;
			double dx = (this.level.random.nextDouble() - this.level.random.nextDouble()) * scale;
			double dy = (this.level.random.nextDouble() - this.level.random.nextDouble()) * scale;
			double dz = (this.level.random.nextDouble() - this.level.random.nextDouble()) * scale;
			this.level.addParticle(options, x, y, z, dx, dy, dz);
		}
		this.remove();
	}

	public ParticleOptions getSubParticle() {
		return new SplinterParticleData(this.blockState);
	}

	public static class Provider implements ParticleProvider<SplinterBurstParticleData> {
		@Override
		public Particle createParticle(SplinterBurstParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			SplinterBurstParticle particle = new SplinterBurstParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.blockState(), type.count());
			particle.setLifetime(1);
			return particle;
		}
	}

}
