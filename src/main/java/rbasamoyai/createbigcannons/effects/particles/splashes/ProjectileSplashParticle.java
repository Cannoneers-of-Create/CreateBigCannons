package rbasamoyai.createbigcannons.effects.particles.splashes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class ProjectileSplashParticle extends NoRenderParticle {

	private final float scale;
	private final float light;

	ProjectileSplashParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float r, float g, float b, float light) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.rCol = r;
		this.gCol = g;
		this.bCol = b;

		this.scale = (float) Math.sqrt(dx * dx + dy * dy + dz * dz) * 2;
		this.light = light;
	}

	@Override
	public void tick() {
		int particleCount = switch (CBCClientCommon.getParticleStatus()) {
            case ALL -> 10;
            case DECREASED -> 5;
            case MINIMAL -> 2;
        };
		double velScale = 1 - (double) this.age / (double) this.lifetime;
		Vec3 vel = new Vec3(this.xd, this.yd, this.zd).scale(velScale);
		double length = vel.length();
		int lifetime = Math.max(Mth.ceil(80 * length), 40);
		ParticleOptions options = new SplashSprayParticleData(this.rCol, this.gCol, this.bCol, this.scale, this.light, lifetime);
		if (this.age < 2 && this.scale >= 0.5) {
			Vec3 focalPoint = new Vec3(this.x, this.y, this.z).add(vel.scale(6));
			double areaScale = 8 * length;
			for (int i = 0; i < particleCount; ++i) {
				double rx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * areaScale;
				double ry = this.y - 0.5;
				double rz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * areaScale;
				double dx1 = (focalPoint.x - rx) * 0.01 + (this.random.nextDouble() - this.random.nextDouble()) * 0.025;
				double dy1 = (focalPoint.y - ry) * 0.03 + (this.random.nextDouble() - this.random.nextDouble()) * 0.3;
				double dz1 = (focalPoint.z - rz) * 0.01 + (this.random.nextDouble() - this.random.nextDouble()) * 0.025;
				this.level.addParticle(options, true, rx, ry, rz, dx1, dy1, dz1);
			}
		} else {
			float f = Math.min(this.scale, 1.5f);
			for (int i = 0; i < particleCount; ++i) {
				double rx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * f;
				double ry = this.y + 0.5;
				double rz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * f;
				double dx1 = vel.x + (this.random.nextDouble() - this.random.nextDouble()) * 0.25 * f;
				double dy1 = vel.y + (this.random.nextDouble() - this.random.nextDouble()) * 0.35;
				double dz1 = vel.z + (this.random.nextDouble() - this.random.nextDouble()) * 0.25 * f;
				this.level.addParticle(options, true, rx, ry, rz, dx1, dy1, dz1);
			}
		}
		if (++this.age >= this.lifetime) {
			this.remove();
		}
	}

	public static class Provider implements ParticleProvider<ProjectileSplashParticleData> {
		@Override
		public Particle createParticle(ProjectileSplashParticleData data, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			ProjectileSplashParticle particle = new ProjectileSplashParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.r(), data.g(), data.b(), data.light());
			particle.setLifetime(11);
			return particle;
		}
	}

}
