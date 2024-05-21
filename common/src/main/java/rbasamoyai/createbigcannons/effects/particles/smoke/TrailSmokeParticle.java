package rbasamoyai.createbigcannons.effects.particles.smoke;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class TrailSmokeParticle extends TextureSheetParticle {

	TrailSmokeParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
		super(level, x, y, z);
		this.gravity = 3.0E-6F;
		this.xd = xd;
		this.yd = yd + this.random.nextDouble() / 500d;
		this.zd = zd;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
			this.xd += this.random.nextFloat() / 5000f * (this.random.nextBoolean() ? 1 : -1);
			this.zd += this.random.nextFloat() / 5000f * (this.random.nextBoolean() ? 1 : -1);
			this.yd -= this.gravity;
			this.move(this.xd, this.yd, this.zd);
			if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
				this.alpha -= 0.015F;
			}
		} else {
			this.remove();
		}
	}

	@Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

	public static class Provider implements ParticleProvider<TrailSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(TrailSmokeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			TrailSmokeParticle particle = new TrailSmokeParticle(level, x, y, z, dx, dy, dz);
			particle.scale(3.0f);
			particle.setSize(0.25f, 0.25f);
			particle.setAlpha(0.35f);
			particle.setLifetime(data.lifetime());
			particle.pickSprite(this.sprites);
			return particle;
		}
	}

}
