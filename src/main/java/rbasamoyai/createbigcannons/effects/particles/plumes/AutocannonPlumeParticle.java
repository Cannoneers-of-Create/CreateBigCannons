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

public class AutocannonPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float scale;
	private final boolean visible;

	// TODO: remove once Create #7232 is fixed
	private final boolean isPonderWorld;

	AutocannonPlumeParticle(ClientLevel level, double x, double y, double z, Vec3 direction, float scale) {
		super(level, x, y, z);
		this.direction = direction;
		this.scale = scale;
		this.visible = CBCClientCommon.getParticleStatus() == ParticleStatus.ALL && CBCConfigs.CLIENT.showAutocannonPlumes.get();

		this.lifetime = 5;

		this.gravity = 0;
		this.setParticleSpeed(0, 0, 0);

		// TODO remove once Create #7232 is fixed
		this.isPonderWorld = level instanceof WrappedClientWorld wrapped && wrapped.getWrappedWorld() instanceof PonderWorld;
	}

	@Override
	public void tick() {
		if (!this.visible) {
			this.remove();
			return;
		}
		Vec3 right = this.direction.cross(new Vec3(Direction.UP.step()));
		Vec3 up = this.direction.cross(right);

		int count = Mth.ceil(this.scale * 2);
		for (int i = 0; i < count; ++i) {
			double dirScale = this.scale * (0.05d + 0.01d * this.random.nextDouble());
			double dirPerpScale = this.scale * 0.05f;
			Vec3 vel = this.direction.scale(dirScale)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(0, 0.02, 0);
			// TODO revert when Create #7232 is fixed
//			this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			if (this.isPonderWorld) {
				this.level.addParticle(ParticleTypes.CLOUD, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			} else {
				this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			}
		}
		if (this.age == 0) {
			int count1 = Math.max(5, Mth.ceil(this.scale * 8));
			for (int i = 0; i < count1; ++i) {
				double dirScale = this.scale * (0.05d + 0.01d * this.random.nextDouble());
				Vec3 vel = this.direction.scale(dirScale);
				// TODO revert when Create #7232 is fixed
//				this.level.addParticle(ParticleTypes.FLAME, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				if (this.isPonderWorld) {
					this.level.addParticle(ParticleTypes.FLAME, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				} else {
					this.level.addParticle(ParticleTypes.FLAME, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
				}
			}
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<AutocannonPlumeParticleData> {
		public Particle createParticle(AutocannonPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new AutocannonPlumeParticle(level, x, y, z, new Vec3(dx, dy, dz), data.scale());
		}
	}

}
