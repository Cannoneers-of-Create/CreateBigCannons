package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import rbasamoyai.createbigcannons.effects.particles.smoke.DebrisSmokeParticleData;

public class DebrisSmokeBurstParticle extends NoRenderParticle {

	DebrisSmokeBurstParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
	}

	@Override
	public void tick() {
		ParticleOptions options = new DebrisSmokeParticleData(3);
		for (int i = 0; i < 10; ++i) {
			double rx = this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1;
			double ry = this.y + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1;
			double rz = this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.1;
			double dx1 = this.xd * 0.2 + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			double dy1 = this.yd * 0.2 + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			double dz1 = this.zd * 0.2 + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			this.level.addParticle(options, true, rx, ry, rz, dx1, dy1, dz1);
		}
		this.remove();
	}

	public static class Provider implements ParticleProvider<DebrisSmokeBurstParticleData> {
		@Override
		public Particle createParticle(DebrisSmokeBurstParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			return new DebrisSmokeBurstParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}
