package rbasamoyai.createbigcannons.effects.particles.explosions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.FlakSmokeParticleData;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FlakCloudParticle extends NoRenderParticle {

	private final List<TrailSubparticle> trails = new LinkedList<>();

	FlakCloudParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z, dx, dy, dz);
		ParticleStatus status = Minecraft.getInstance().options.particles;
		if (CBCConfigs.CLIENT.showExtraFlakTrails.get()) {
			int count = switch (status) {
				case ALL -> 12 + level.random.nextInt(5);
				case DECREASED -> 4 + level.random.nextInt(3);
				case MINIMAL -> 0;
			};
			for (int i = 0; i < count; ++i) {
				double dx1 = this.random.nextDouble() - this.random.nextDouble();
				double dy1 = this.random.nextDouble() - this.random.nextDouble();
				double dz1 = this.random.nextDouble() - this.random.nextDouble();
				double rx = this.random.nextDouble() - this.random.nextDouble();
				double ry = this.random.nextDouble() - this.random.nextDouble();
				double rz = this.random.nextDouble() - this.random.nextDouble();
				int lifetime = 5;
				this.trails.add(new TrailSubparticle(new Vec3(rx, ry, rz).scale(0.25), new Vec3(dx1, dy1, dz1).scale(2), 0.5, lifetime));
			}
		}
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
	}

	@Override
	public void tick() {
		if (!CBCConfigs.CLIENT.showFlakClouds.get()) {
			this.remove();
			return;
		}
		ParticleStatus status = Minecraft.getInstance().options.particles;
		int count = switch (status) {
            case ALL -> 30;
            case DECREASED -> 20;
            case MINIMAL -> 10;
        };
		if (this.age == 0) {
			for (int i = 0; i < count; ++i) {
				double dx = this.random.nextDouble() - this.random.nextDouble();
				double dy = this.random.nextDouble() - this.random.nextDouble();
				double dz = this.random.nextDouble() - this.random.nextDouble();
				double rx = this.random.nextDouble() - this.random.nextDouble();
				double ry = this.random.nextDouble() - this.random.nextDouble();
				double rz = this.random.nextDouble() - this.random.nextDouble();
				int lifetime = 45 + this.random.nextInt(80);
				this.level.addParticle(new FlakSmokeParticleData(lifetime, 3), true, this.x + rx * 0.25,
					this.y + ry * 0.25, this.z + rz * 0.25, dx * 0.5, dy * 0.5, dz * 0.5);
			}
			if (status == ParticleStatus.ALL && CBCConfigs.CLIENT.showExtraFlakCloudFlames.get()) {
				for (int i = 0; i < 20; ++i) {
					double dx = this.random.nextDouble() - this.random.nextDouble();
					double dy = this.random.nextDouble() - this.random.nextDouble();
					double dz = this.random.nextDouble() - this.random.nextDouble();
					double rx = this.random.nextDouble() - this.random.nextDouble();
					double ry = this.random.nextDouble() - this.random.nextDouble();
					double rz = this.random.nextDouble() - this.random.nextDouble();
					this.level.addParticle(ParticleTypes.FLAME, true, this.x + rx * 0.25,
						this.y + ry * 0.25, this.z + rz * 0.25, dx * 0.3, dy * 0.3, dz * 0.3);
				}
			}
			if (status == ParticleStatus.ALL && CBCConfigs.CLIENT.showExtraFlakCloudShockwave.get()) {
				this.level.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 0, 0, 0);
			}
		}
		if (status != ParticleStatus.MINIMAL) {
			int trailSteps = status == ParticleStatus.ALL ? 5 : 2;
			for (Iterator<TrailSubparticle> iter = this.trails.iterator(); iter.hasNext(); ) {
				TrailSubparticle trail = iter.next();
				if (this.age > trail.lifetime) {
					iter.remove();
					continue;
				}
				Vec3 origin = trail.calculateDisplacement(this.age);
				Vec3 next = trail.calculateDisplacement(this.age + 1);
				for (int i = 0; i <= trailSteps; ++i) {
					Vec3 pos = origin.lerp(next, (double) i / (double) trailSteps).add(this.x, this.y, this.z);
					double dx = this.random.nextDouble() - this.random.nextDouble();
					double dy = this.random.nextDouble() - this.random.nextDouble();
					double dz = this.random.nextDouble() - this.random.nextDouble();
					double rx = this.random.nextDouble() - this.random.nextDouble();
					double ry = this.random.nextDouble() - this.random.nextDouble();
					double rz = this.random.nextDouble() - this.random.nextDouble();
					int lifetime = 20 + this.random.nextInt(5);
					this.level.addParticle(new FlakSmokeParticleData(lifetime, 1), true, pos.x + rx * 0.25,
						pos.y + ry * 0.25, pos.z + rz * 0.01, dx * 0.01, dy * 0.01, dz * 0.01);
				}
			}
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<FlakCloudParticleData> {
		@Override
		public Particle createParticle(FlakCloudParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			FlakCloudParticle particle = new FlakCloudParticle(level, x, y, z, dx, dy, dz);
			particle.setSize(0.25f, 0.25f);
			particle.setLifetime(30);
			return particle;
		}
	}

	private record TrailSubparticle(Vec3 displacement, Vec3 vel, double drag, int lifetime) {
		public Vec3 calculateDisplacement(int ticks) {
			if (ticks <= 0 || this.drag <= 1e-2d)
				return this.displacement;
			if (this.drag == 1)
				return this.displacement.add(this.vel.scale(ticks));
			double geo = (1 - Math.pow(this.drag, ticks)) / (1 - this.drag);
			return this.displacement.add(this.vel.scale(geo));
		}
	}

}
