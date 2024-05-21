package rbasamoyai.createbigcannons.effects.particles.plumes;

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
import rbasamoyai.createbigcannons.effects.particles.smoke.CannonSmokeParticleData;

public class BigCannonPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float scale;
	private final boolean visible;

	BigCannonPlumeParticle(ClientLevel level, double x, double y, double z, Vec3 direction, float scale) {
		super(level, x, y, z);
		this.direction = direction;
		this.scale = scale;
		Minecraft mc = Minecraft.getInstance();
		this.visible = mc.options.particles == ParticleStatus.ALL && CBCConfigs.CLIENT.showBigCannonPlumes.get();

		this.lifetime = 0;

		this.gravity = 0;
		this.setParticleSpeed(0, 0, 0);
	}

	@Override
	public void tick() {
		if (!this.visible) {
			this.remove();
			return;
		}
		Vec3 right = this.direction.cross(new Vec3(Direction.UP.step()));
		Vec3 up = this.direction.cross(right);

		int count = Mth.ceil(this.scale * 40);
		for (int i = 0; i < count; ++i) {
			int x = Math.floorDiv(i, 10) * 10;
			double progress = (double) x / (double) count;
			double dirScale = this.scale * (0.5 + 1.25d * this.random.nextDouble());
			double dirPerpScale = this.scale * 0.5f;
			Vec3 vel = this.direction.scale(dirScale + progress * 2d)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale));
			int shiftTime = 3 + this.random.nextInt(2);
			int startShiftTime = 10 + Mth.ceil(progress * 5);
			int lifetime = 180 + this.random.nextInt(40);
			this.level.addParticle(new CannonSmokeParticleData(this.scale, shiftTime, startShiftTime, lifetime, 0.92f - (float) progress * 0.08f),
				true, this.x, this.y, this.z, vel.x, vel.y, vel.z);

			dirScale *= 0.9f;
			dirPerpScale *= 0.9f;
			Vec3 vel1 = this.direction.scale(dirScale)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.scale(1f);
			int shiftTime1 = 3 + this.random.nextInt(2);
			int startShiftTime1 = 6;
			int lifetime1 = 180 + this.random.nextInt(40);
			this.level.addParticle(new CannonSmokeParticleData(this.scale, shiftTime1, startShiftTime1, lifetime1, 0.92f),
				true, this.x, this.y, this.z, vel1.x, vel1.y, vel1.z);
		}

		float scale2 = this.scale * 1.5f;
		float count2 = this.scale * 50;
		for (int i = 0; i < count2; ++i) {
			Vec3 vel = this.direction.scale(0.5)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2));
			this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<BigCannonPlumeParticleData> {
		public Particle createParticle(BigCannonPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new BigCannonPlumeParticle(level, x, y, z, new Vec3(dx, dy, dz), data.scale());
		}
	}

}
