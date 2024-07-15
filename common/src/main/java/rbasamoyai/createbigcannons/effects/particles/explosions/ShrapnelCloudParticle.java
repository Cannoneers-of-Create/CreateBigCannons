package rbasamoyai.createbigcannons.effects.particles.explosions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.ShrapnelSmokeParticleData;

public class ShrapnelCloudParticle extends NoRenderParticle {

	ShrapnelCloudParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z, dx, dy, dz);
	}

	@Override
	public void tick() {
		if (!this.shouldShow()) {
			this.remove();
			return;
		}
		ParticleStatus status = Minecraft.getInstance().options.particles;
		int count = switch (status) {
            case ALL -> 20;
            case DECREASED -> 10;
            case MINIMAL -> 5;
        };
		for (int i = 0; i < count; ++i) {
			double dx = this.random.nextDouble() - this.random.nextDouble();
			double dy = this.random.nextDouble() - this.random.nextDouble();
			double dz = this.random.nextDouble() - this.random.nextDouble();
			double rx = this.random.nextDouble() - this.random.nextDouble();
			double ry = this.random.nextDouble() - this.random.nextDouble();
			double rz = this.random.nextDouble() - this.random.nextDouble();
			int lifetime = 85 + this.random.nextInt(80);
			this.level.addParticle(new ShrapnelSmokeParticleData(lifetime), true, this.x + rx * 0.25,
				this.y + ry * 0.25, this.z + rz * 0.25, dx * 0.7, dy * 0.7, dz * 0.7);
		}
		if (status == ParticleStatus.ALL && this.flamesVisible()) {
			for (int i = 0; i < 20; ++i) {
				double dx = this.random.nextDouble() - this.random.nextDouble();
				double dy = this.random.nextDouble() - this.random.nextDouble();
				double dz = this.random.nextDouble() - this.random.nextDouble();
				double rx = this.random.nextDouble() - this.random.nextDouble();
				double ry = this.random.nextDouble() - this.random.nextDouble();
				double rz = this.random.nextDouble() - this.random.nextDouble();
				this.level.addParticle(ParticleTypes.FLAME, true, this.x + rx * 0.25,
					this.y + ry * 0.25, this.z + rz * 0.25, dx * 0.45, dy * 0.45, dz * 0.45);
			}
		}
		if (status == ParticleStatus.ALL && this.shockwaveVisible()) {
			this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 0, 0, 0);
		}
		super.tick();
	}

	protected boolean shouldShow() { return CBCConfigs.CLIENT.showShrapnelClouds.get(); }
	protected boolean flamesVisible() { return CBCConfigs.CLIENT.showExtraShrapnelCloudFlames.get(); }
	protected boolean shockwaveVisible() { return CBCConfigs.CLIENT.showExtraShrapnelCloudShockwave.get(); }

	public static class Provider implements ParticleProvider<ShrapnelCloudParticleData> {
		@Override
		public Particle createParticle(ShrapnelCloudParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			ShrapnelCloudParticle particle = new ShrapnelCloudParticle(level, x, y, z, dx, dy, dz);
			particle.setSize(0.25f, 0.25f);
			particle.setLifetime(1);
			return particle;
		}
	}

}
