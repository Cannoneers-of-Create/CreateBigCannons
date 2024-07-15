package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.math.Vector3f;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class SparkBurstParticle extends NoRenderParticle {

	private final boolean deflect;
	private final int count;

	SparkBurstParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float r, float g,
                       float b, boolean deflect, int count) {
		super(level, x, y, z);
        this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.rCol = r;
		this.gCol = g;
		this.bCol = b;
		this.deflect = deflect;
		this.count = count;
	}

	@Override
	public void tick() {
		Vec3 vel = new Vec3(this.xd, this.yd, this.zd);
		double velScale = this.deflect ? 0.5 : 0.75;
		double velScale1 = this.deflect ? 1 : 0.75;
		double velScale2 = this.deflect ? 0.015 : 0.035;
		Vec3 forward = vel.normalize();
		Vec3 vec31 = forward.cross(new Vec3(0, 1, 0)).lengthSqr() < 1e-4d ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
		Vec3 up = forward.cross(vec31);
		Vec3 right = forward.cross(up);

		int count = switch (CBCClientCommon.getParticleStatus()) {
            case ALL -> this.count;
            case DECREASED -> this.count / 2;
            case MINIMAL -> this.count / 4;
        };

		ParticleOptions particle = new SparkParticleData(this.rCol, this.gCol, this.bCol);
		for (int i = 0; i < count; ++i) {
			double rx = this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			double ry = this.y + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			double rz = this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 0.5;
			double d1 = 1 + (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale;
			double d2 = (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale1;
			double d3 = (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale1;
			double d4 = (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale2;
			double d5 = (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale2;
			double d6 = (this.level.random.nextDouble() - this.level.random.nextDouble()) * velScale2;
			Vec3 vel1 = vel.scale(d1).add(up.scale(d2)).add(right.scale(d3)).add(d4, d5, d6);
			this.level.addParticle(particle, true, rx, ry, rz, vel1.x, vel1.y, vel1.z);
		}
		this.remove();
	}

	public static class Provider implements ParticleProvider<SparkBurstParticleData> {
		@Override
		public Particle createParticle(SparkBurstParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			Vector3f color = type.color();
			SparkBurstParticle particle = new SparkBurstParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, color.x(),
				color.y(), color.z(), type.deflect(), type.count());
			return particle;
		}
	}
}
