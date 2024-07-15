package rbasamoyai.createbigcannons.effects.particles.explosions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.ShellExplosionSmokeParticleData;

public class ShellExplosionCloudParticle extends NoRenderParticle {

	private final float power;
	private final List<TrailSubparticle> trails = new LinkedList<>();
	private final boolean isPlume;

	ShellExplosionCloudParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float power, boolean isPlume) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.power = power;
		this.isPlume = isPlume;

		Minecraft minecraft = Minecraft.getInstance();
		if (CBCConfigs.CLIENT.showExtraShellExplosionTrails.get()) {
			double secondaryVelScale = this.power * 0.35;
			int secondaryCount = switch(minecraft.options.particles) {
				case ALL -> 12 + this.random.nextInt(6);
				case DECREASED -> 6 + this.random.nextInt(3);
				case MINIMAL -> 4;
			};
			double gravity = this.isPlume ? -0.5 : -0.1;
			for (int i = 0; i < secondaryCount; ++i) {
				double rx = this.random.nextDouble() - this.random.nextDouble();
				double ry = this.random.nextDouble() - this.random.nextDouble();
				double rz = this.random.nextDouble() - this.random.nextDouble();
				double dx1 = this.random.nextDouble() - this.random.nextDouble();
				double dy1 = this.random.nextDouble() - this.random.nextDouble();
				if (this.isPlume)
					dy1 += 2;
				double dz1 = this.random.nextDouble() - this.random.nextDouble();
				int lifetime = 5;
				this.trails.add(new TrailSubparticle(new Vec3(rx, ry, rz).scale(2.5),
					new Vec3(dx1, dy1, dz1).scale(secondaryVelScale), 0.85, gravity, lifetime));
			}
		}
	}

	@Override
	public void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		int PLUME_AGE = 5;
		if (this.age < PLUME_AGE) {
			float primaryScale = this.power * 2f;
			int plumes = switch (minecraft.options.particles) {
				case ALL -> 20;
				case DECREASED -> 10;
				case MINIMAL -> 5;
			};
			double velScale = this.power * 0.25;
			velScale *= 1 - (float) this.age / (float) PLUME_AGE;
			double displacementScale = this.power * 0.35;
			for (int i = 0; i <= plumes; ++i) {
				double rx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * displacementScale;
				double ry = this.random.nextDouble() - this.random.nextDouble();
				if (this.isPlume)
					ry = Math.abs(ry);
				ry *= displacementScale;
				ry += this.y + 0.5;
				double rz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * displacementScale;
				double dx = (this.random.nextDouble() - this.random.nextDouble()) * velScale;
				double dy = (this.random.nextDouble() - this.random.nextDouble());
				if (this.isPlume)
					dy += 1.2;
				dy *= velScale;
				double dz = (this.random.nextDouble() - this.random.nextDouble()) * velScale;
				int lifetime = 160 + this.random.nextInt(80);
				this.level.addParticle(new ShellExplosionSmokeParticleData(lifetime, primaryScale), true, rx, ry, rz, dx, dy, dz);
			}
		}
		int trailSteps = minecraft.options.particles == ParticleStatus.ALL ? 2 : 1;
		float secondaryScale = this.power * 0.75f;
		for (Iterator<TrailSubparticle> iter = this.trails.iterator(); iter.hasNext(); ) {
			TrailSubparticle trail = iter.next();
			if (this.age > trail.lifetime) {
				iter.remove();
				continue;
			}
			Vec3 origin = trail.calculateDisplacement(this.age);
			Vec3 next = trail.calculateDisplacement(this.age + 1);
			double velScale = 0.125;
			double displacementScale = 0.1;
			for (int i = 0; i <= trailSteps; ++i) {
				Vec3 pos = origin.lerp(next, (double) i / (double) trailSteps).add(this.x, this.y, this.z);
				double rx = pos.x + (this.random.nextDouble() - this.random.nextDouble()) * displacementScale;
				double ry = pos.y + (this.random.nextDouble() - this.random.nextDouble()) * displacementScale;
				double rz = pos.z + (this.random.nextDouble() - this.random.nextDouble()) * displacementScale;
				double dx = (this.random.nextDouble() - this.random.nextDouble()) * velScale;
				double dy = (this.random.nextDouble() - this.random.nextDouble()) * velScale;
				double dz = (this.random.nextDouble() - this.random.nextDouble()) * velScale;
				int lifetime = 30 + this.random.nextInt(15);
				this.level.addParticle(new ShellExplosionSmokeParticleData(lifetime, secondaryScale), true, rx, ry, rz, dx, dy, dz);
			}
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<ShellExplosionCloudParticleData> {
		@Nullable
		@Override
		public Particle createParticle(ShellExplosionCloudParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			if (!CBCConfigs.CLIENT.showShellExplosionClouds.get())
				return null;
			ShellExplosionCloudParticle particle = new ShellExplosionCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.scale(), type.isPlume());
			particle.setLifetime(10);
			return particle;
		}
	}

	private record TrailSubparticle(Vec3 displacement, Vec3 vel, double drag, double gravity, int lifetime) {
		public Vec3 calculateDisplacement(int ticks) {
			if (ticks <= 0)
				return this.displacement;
			if (this.drag == 1)
				return this.displacement.add(this.vel.scale(ticks));
			double geo = (1 - Math.pow(this.drag, ticks)) / (1 - this.drag);
			return this.displacement.add(this.vel.scale(geo)).add(0, this.gravity * ticks, 0);
		}
	}

}
