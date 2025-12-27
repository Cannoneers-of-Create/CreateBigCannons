package rbasamoyai.createbigcannons.effects.particles.smoke;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class DebrisSmokeParticle extends TrailSmokeParticle {

	DebrisSmokeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
		super(level, x, y, z, xd, yd, zd);
		this.gravity = 0.0025f;
	}

	public static class Provider implements ParticleProvider<DebrisSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Nullable
		@Override
		public Particle createParticle(DebrisSmokeParticleData data, ClientLevel level, double x, double y, double z,
									   double dx, double dy, double dz) {
			DebrisSmokeParticle particle = new DebrisSmokeParticle(level, x, y, z, dx, dy, dz);
			float f = data.scale();
			float f1 = 0.25f * data.scale();
			particle.scale(3.0f * f);
			particle.setSize(f1, f1);
			particle.setAlpha(0.8f);
			particle.setLifetime(120);
			particle.friction = 0.92f;
			particle.pickSprite(this.sprites);
			return particle;
		}
	}

}
