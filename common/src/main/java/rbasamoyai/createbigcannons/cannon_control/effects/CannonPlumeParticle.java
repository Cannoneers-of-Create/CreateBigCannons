package rbasamoyai.createbigcannons.cannon_control.effects;

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
		this.plumesSetting = mc.options.particles().get() == ParticleStatus.ALL ? CBCConfigs.CLIENT.showCannonPlumes.get() : PlumeSetting.OFF;

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
		float t1 = Mth.cos(tScale * Mth.PI);
		t1 = t1 * t1 * Mth.sqrt(tScale) + 0.25f;

		for (int i = 0; i < count; ++i) {
			double dirScale = this.scale * (0.5 + 0.1d * this.random.nextDouble()) * t1;
			Vec3 vel = this.direction.scale(dirScale)
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.25f))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * this.scale * 0.25f))
					.scale(1f);

			if (this.plumesSetting == PlumeSetting.LEGACY) {
				this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			} else {
				this.level.addParticle(new CannonSmokeParticleData(this.scale, new Vec3(0.85, 0.85, 0.85), new Vec3(0.75, 0.75, 0.75), 60),
					true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			}
		}

		if (this.age == 0) {
			if (this.plumesSetting == PlumeSetting.LEGACY) {
				this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 0, 0, 0);
			} else {
				float scale1 = this.scale * 0.5f;
				for (int i = 0; i < Mth.ceil(this.scale * 2); ++i) {
					Vec3 vel = this.direction.scale(0.5)
							.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1))
							.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1))
							.scale(1);
					this.level.addParticle(new CannonSmokeParticleData(this.scale * 0.25f, new Vec3(1, 96 / 255.0, 0), new Vec3(0.92, 0.92, 0.92), 20), this.x, this.y, this.z, vel.x, vel.y, vel.z);
				}
			}
			float scale1 = this.scale * 1.5f;
			float count1 = this.scale * 50;
			for (int i = 0; i < count1; ++i) {
				Vec3 vel = this.direction.scale(0.5)
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale1));
				this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
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
