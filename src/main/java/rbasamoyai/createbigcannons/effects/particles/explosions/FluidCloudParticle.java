package rbasamoyai.createbigcannons.effects.particles.explosions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class FluidCloudParticle extends ShrapnelCloudParticle {

	FluidCloudParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z, dx, dy, dz);
	}

	@Override protected boolean shouldShow() { return CBCConfigs.client().showFluidShellClouds.get(); }

	@Override protected boolean flamesVisible() { return CBCConfigs.client().showExtraFluidShellCloudFlames.get(); }

	@Override protected boolean shockwaveVisible() { return CBCConfigs.client().showExtraFluidShellCloudShockwave.get(); }

	public static class Provider implements ParticleProvider<FluidCloudParticleData> {
		@Override
		public Particle createParticle(FluidCloudParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			FluidCloudParticle particle = new FluidCloudParticle(level, x, y, z, dx, dy, dz);
			particle.setSize(0.25f, 0.25f);
			particle.setLifetime(1);
			return particle;
		}
	}

}
