package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class FluidBlobParticle extends NoRenderParticle {

	private final float scale;
	private final int particleCount;
	private final EndFluidStack fluid;

	FluidBlobParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float scale, EndFluidStack fluid) {
		super(level, x, y, z, dx, dy, dz);
		this.scale = scale;
		this.particleCount = CBCConfigs.CLIENT.fluidBlobParticleCount.get();
		this.lifetime = 0;
		this.fluid = fluid;
	}

	@Override
	public void tick() {
		if (this.particleCount == 0) return;

		super.tick();

		ParticleOptions options = IndexPlatform.createFluidDripParticle(this.fluid);
		for (int i = 0; i < this.particleCount; ++i) {
			double rx = this.random.nextGaussian() * this.scale;
			double ry = this.random.nextGaussian() * this.scale;
			double rz = this.random.nextGaussian() * this.scale;
			double rdx = this.random.nextGaussian() * 0.1d;
			double rdy = this.random.nextGaussian() * 0.1d;
			double rdz = this.random.nextGaussian() * 0.1d;
			this.level.addParticle(options, this.x + rx, this.y + ry, this.z + rz, this.xd + rdx, this.yd + rdy, this.zd + rdz);
		}
	}

	public static class Provider implements ParticleProvider<FluidBlobParticleData> {
		public Particle createParticle(FluidBlobParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new FluidBlobParticle(level, x, y, z, dx, dy, dz, data.scale(), data.fluid());
		}
	}

}
