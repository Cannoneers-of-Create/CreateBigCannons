package rbasamoyai.createbigcannons.cannonmount;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CannonPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float scale;
	
	private int life = 0;
	private final int lifetime = 20;
	
	CannonPlumeParticle(ClientLevel level, double x, double y, double z, Vec3 direction, float scale) {
		super(level, x, y, z);
		this.direction = direction;
		this.scale = scale;
	}
	
	@Override
	public void tick() {
		int count = Mth.ceil(this.scale * 20);
		
		Vec3 right = this.direction.cross(new Vec3(Direction.UP.step()));
		Vec3 up = this.direction.cross(right);
		
		for (int i = 0; i < count; ++i) {
			double dirScale = this.scale * (0.9d + 0.1d * this.random.nextDouble());
			Vec3 vel = this.direction.scale(dirScale)
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.5f))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.5f))
					.scale(0.4f);
			
			this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.x, this.y, this.z, vel.x, vel.y, vel.z);
		}
		
		if (this.life == 0) {
			this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 0, 0, 0);
			float scale1 = this.scale / 2.0f;
			float count1 = this.scale * 50;
			float speed = 0.5f;
			for (int i = 0; i < count1; ++i) {
				double rx = this.random.nextGaussian() * scale1;
				double ry = this.random.nextGaussian() * scale1;
				double rz = this.random.nextGaussian() * scale1;
				double dx = this.random.nextGaussian() * speed;
				double dy = this.random.nextGaussian() * speed;
				double dz = this.random.nextGaussian() * speed;
				this.level.addParticle(ParticleTypes.CLOUD, rx + this.x, ry + this.y, rz + this.z, dx, dy, dz);
			}
		}
		
		++this.life;
		if (this.life == this.lifetime) {
			this.remove();
		}
	}
	
	public static class Provider implements ParticleProvider<CannonPlumeParticleData> {
		public Particle createParticle(CannonPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new CannonPlumeParticle(level, x, y, z, new Vec3(dx, dy, dz), data.scale());
		}
	}
	
}
