package rbasamoyai.createbigcannons.effects.particles.impacts;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;

public class DebrisMatterParticle extends NoRenderParticle {

	private final boolean deflect;
	private final boolean forceDisplay;
	private final BlockState blockState;

	DebrisMatterParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, boolean deflect,
						 boolean forceDisplay, BlockState blockState) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.deflect = deflect;
		this.forceDisplay = forceDisplay;
		this.blockState = blockState;
	}

	@Override
	public void tick() {
		double velScale = this.deflect ? 0.25 : 0.15;
		double offsetScale = this.deflect ? 0.15 : 0.35;
		int count = this.deflect ? 20 : 40;
		ParticleOptions particle = new CBCBlockParticleData(this.blockState);
		for (int i = 0; i < count; ++i) {
			double rx = this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 1.5;
			double ry = this.y + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 1.5;
			double rz = this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 1.5;
			double dx1 = this.xd * velScale + (this.level.random.nextDouble() - this.level.random.nextDouble()) * offsetScale;
			double dy1 = this.yd * velScale + (this.level.random.nextDouble() - this.level.random.nextDouble()) * offsetScale;
			double dz1 = this.zd * velScale + (this.level.random.nextDouble() - this.level.random.nextDouble()) * offsetScale;
			this.level.addParticle(particle, this.forceDisplay, rx, ry, rz, dx1, dy1, dz1);
		}
		this.remove();
	}

	public static class Provider implements ParticleProvider<DebrisMatterParticleData> {
		@Override
		public Particle createParticle(DebrisMatterParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			return new DebrisMatterParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.deflect(), type.forceDisplay(), type.blockState());
		}
	}

}
