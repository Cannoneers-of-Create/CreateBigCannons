package rbasamoyai.createbigcannons.effects.particles.plumes;

import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedClientWorld;

import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.CannonSmokeParticleData;

public class BigCannonPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float size;
	private final float power;

	// TODO: remove once Create #7232 is fixed
	private final boolean isPonderWorld;

	BigCannonPlumeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float size, float power) {
		super(level, x, y, z);
		this.direction = new Vec3(dx, dy, dz);
		this.size = size;
		this.power = power;
		this.gravity = 0;
		this.friction = 0.90f;
		float f = this.power / 4;
		this.setParticleSpeed(dx * f, dy * f, dz * f);

		// TODO remove once Create #7232 is fixed
		this.isPonderWorld = level instanceof WrappedClientWorld wrapped && wrapped.getWrappedWorld() instanceof PonderWorld;
	}

	@Override
	public void tick() {
		if (!CBCConfigs.CLIENT.showBigCannonPlumes.get()) {
			this.remove();
			return;
		}
		ParticleStatus status = CBCClientCommon.getParticleStatus();
		Vec3 right = this.direction.cross(new Vec3(Direction.UP.step()));
		Vec3 up = this.direction.cross(right);
		double progress = this.lifetime == 0 ? 1 : Mth.clamp((float) this.age / (float) this.lifetime, 0, 1);

		float smallScale = this.size * 0.25f;
		int countScale = switch (status) {
            case ALL -> 5;
            case DECREASED -> 3;
            case MINIMAL -> 1;
        };
		int count = Math.min(3, Mth.ceil(smallScale * countScale));
		for (int i = 0; i < count; ++i) {
			double dirScale = 0.3 * progress + 0.8 + this.random.nextDouble() * 0.25;
			double dirPerpScale = smallScale * 0.25f;
			Vec3 vel = this.direction.scale(dirScale)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale));
			int lifetime = 30 + this.random.nextInt(10) + Mth.ceil(10 * progress) + (int) Math.ceil(this.power * this.power) / 2;

			// TODO revert when Create #7232 is fixed
//			this.level.addParticle(new CannonSmokeParticleData(this.power, smallScale, lifetime, 0.95f),
//				true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			if (this.isPonderWorld) {
				this.level.addParticle(new CannonSmokeParticleData(this.power, smallScale, lifetime, 0.95f), this.x, this.y, this.z, vel.x, vel.y, vel.z);
			} else {
				this.level.addParticle(new CannonSmokeParticleData(this.power, smallScale, lifetime, 0.95f),
					true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			}
			// TODO revert when Create #7232 is fixed
		}

		if (this.age == 0 && status == ParticleStatus.ALL && CBCConfigs.CLIENT.showExtraBigCannonSmoke.get()) {
			float scale2 = smallScale * 0.25f;
			int count2 = (int) Math.floor(smallScale * 10);

			for (int i = 0; i < count2; ++i) {
				Vec3 vel = this.direction.scale(0.5)
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2));
				// TODO revert when Create #7232 is fixed
//				this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				if (this.isPonderWorld) {
					this.level.addParticle(ParticleTypes.CLOUD, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				} else {
					this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				}
				// TODO revert when Create #7232 is fixed
			}
		}
		if (this.age < 5 && status == ParticleStatus.ALL && CBCConfigs.CLIENT.showExtraBigCannonFlames.get()) {
			float scale2 = 0.05f;
			float count2 = smallScale * 5;
			for (int i = 0; i < count2; ++i) {
				Vec3 vel1 = this.direction.scale(0.05 + 0.2 * this.random.nextDouble())
					.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2))
					.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * scale2));
				// TODO revert when Create #7232 is fixed
//				this.level.addParticle(ParticleTypes.FLAME, false, this.x, this.y, this.z, vel1.x, vel1.y, vel1.z);
				if (this.isPonderWorld) {
					this.level.addParticle(ParticleTypes.FLAME, this.x, this.y, this.z, vel1.x, vel1.y, vel1.z);
				} else {
					this.level.addParticle(ParticleTypes.FLAME, false, this.x, this.y, this.z, vel1.x, vel1.y, vel1.z);
				}
				// TODO revert when Create #7232 is fixed
			}
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<BigCannonPlumeParticleData> {
		public Particle createParticle(BigCannonPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new BigCannonPlumeParticle(level, x, y, z, dx, dy, dz, data.size(), data.power());
		}
	}

}
