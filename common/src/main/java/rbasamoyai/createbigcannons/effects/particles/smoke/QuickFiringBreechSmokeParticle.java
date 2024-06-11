package rbasamoyai.createbigcannons.effects.particles.smoke;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleTypes;

public class QuickFiringBreechSmokeParticle extends NoRenderParticle {

	private final double particlesDx;
	private final double particlesDy;
	private final double particlesDz;

	QuickFiringBreechSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z, 0, 0, 0);
		this.particlesDx = dx;
		this.particlesDy = dy;
		this.particlesDz = dz;
		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
	}

	@Override
	public void tick() {
		Minecraft minecraft = Minecraft.getInstance();
		int count = switch (minecraft.options.particles) {
            case ALL -> 10;
            case DECREASED -> 2;
            case MINIMAL -> 0;
        };
		for (int i = 0; i < count; ++i) {
			double dx = this.particlesDx + (this.random.nextDouble() - this.random.nextDouble()) * 0.005;
			double dy = this.particlesDy + (this.random.nextDouble() - this.random.nextDouble()) * 0.005;
			double dz = this.particlesDz + (this.random.nextDouble() - this.random.nextDouble()) * 0.005;
			this.level.addParticle(ParticleTypes.POOF, this.x, this.y, this.z, dx, dy, dz);
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<QuickFiringBreechSmokeParticleData> {
		@Nullable
		@Override
		public Particle createParticle(QuickFiringBreechSmokeParticleData type, ClientLevel level, double x, double y,
									   double z, double xSpeed, double ySpeed, double zSpeed) {
			QuickFiringBreechSmokeParticle particle = new QuickFiringBreechSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
			particle.setLifetime(2);
			return particle;
		}
	}

}
