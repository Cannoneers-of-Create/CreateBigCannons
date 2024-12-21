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

public class DropMortarPlumeParticle extends NoRenderParticle {

	private final Vec3 direction;
	private final float scale;
	private final boolean visible;

	// TODO: remove once Create #7232 is fixed
	private final boolean isPonderWorld;

	DropMortarPlumeParticle(ClientLevel level, double x, double y, double z, Vec3 direction, float scale) {
		super(level, x, y, z);
		this.direction = direction;
		this.scale = scale;
		this.visible = CBCClientCommon.getParticleStatus() == ParticleStatus.ALL && CBCConfigs.CLIENT.showDropMortarPlumes.get();

		this.lifetime = 0;

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

		int count = Mth.ceil(this.scale * 20);
		for (int i = 0; i < count; ++i) {
			double dirScale = this.scale * (0.0125d + 0.05d * this.random.nextDouble());
			double dirPerpScale = this.scale * 0.125f;
			Vec3 vel = this.direction.scale(dirScale)
				.add(right.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(up.scale((this.random.nextDouble() - this.random.nextDouble()) * dirPerpScale))
				.add(0, 0.025, 0);
			// TODO revert when Create #7232 is fixed
//			this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			if (this.isPonderWorld) {
				this.level.addParticle(ParticleTypes.CLOUD, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			} else {
				this.level.addParticle(ParticleTypes.CLOUD, true, this.x, this.y, this.z, vel.x, vel.y, vel.z);
			}
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<DropMortarPlumeParticleData> {
		public Particle createParticle(DropMortarPlumeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new DropMortarPlumeParticle(level, x, y, z, new Vec3(dx, dy, dz), data.scale());
		}
	}

}
