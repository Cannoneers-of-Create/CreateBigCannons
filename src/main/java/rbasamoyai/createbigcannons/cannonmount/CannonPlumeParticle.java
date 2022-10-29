package rbasamoyai.createbigcannons.cannonmount;

import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float scale;
	private final PlumeSetting plumesSetting;
	
	CannonPlumeParticle(ClientLevel level, double x, double y, double z, Vec3 direction, float scale) {
		super(level, x, y, z);
		this.direction = direction;
		this.scale = scale;
		Minecraft mc = Minecraft.getInstance();
		this.plumesSetting = mc.options.particles == ParticleStatus.ALL ? CBCConfigs.CLIENT.showCannonPlumes.get() : PlumeSetting.OFF;
		
		this.lifetime = 20;
		
		this.gravity = 0;
		this.setParticleSpeed(0, 0, 0);
	}
	
	@Override
	public void tick() {
		if (this.plumesSetting == PlumeSetting.OFF) return;
		
		Vec3 right = this.direction.cross(new Vec3(Direction.UP.step()));
		Vec3 up = this.direction.cross(right);
		
		int count = Mth.ceil(this.scale * (this.plumesSetting == PlumeSetting.LEGACY ? 20 : 2));
		float tScale = this.age >= this.lifetime ? 0 : 1.0f - (float) this.age / (float) this.lifetime;
		
		for (int i = 0; i < count; ++i) {
			double dirScale = this.scale * (0.9d + 0.1d * this.random.nextDouble()) * tScale;
			Vec3 vel = this.direction.scale(dirScale)
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.5f))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.5f))
					.scale(0.4f);
			
			if (this.plumesSetting == PlumeSetting.LEGACY) {
				this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			} else {
				this.level.addParticle(new CannonSmokeParticleData(this.scale, new Vector3f(0.85f, 0.85f, 0.85f), new Vector3f(0.75f, 0.75f, 0.75f), 60), this.x, this.y, this.z, vel.x, vel.y, vel.z);
			}
		}
		
		if (this.age == 0) {
			if (this.plumesSetting == PlumeSetting.LEGACY) {
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
			} else {
				float scale1 = this.scale * 0.5f; 
				for (int i = 0; i < Mth.ceil(this.scale * 2); ++i) {
					Vec3 vel = this.direction.scale(scale1)
							.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1))
							.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1))
							.scale(0.4f);
					this.level.addParticle(new CannonSmokeParticleData(this.scale * 0.25f, new Vector3f(1.0f, 96f / 255f, 0.0f), new Vector3f(0.92f, 0.92f, 0.92f), 20), this.x, this.y, this.z, vel.x, vel.y, vel.z);
				}
			}
		}
		
		super.tick();
	}
	
	public enum PlumeSetting {
		OFF,
		LEGACY,
		DEFAULT
	}
	
	public static class Provider implements ParticleProvider<CannonPlumeParticleData> {
		public Particle createParticle(CannonPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new CannonPlumeParticle(level, x, y, z, new Vec3(dx, dy, dz), data.scale());
		}
	}
	
}
