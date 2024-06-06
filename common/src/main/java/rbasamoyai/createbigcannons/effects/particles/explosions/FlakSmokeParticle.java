package rbasamoyai.createbigcannons.effects.particles.explosions;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class FlakSmokeParticle extends BaseAshSmokeParticle {

	FlakSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, 0.01f, 0.01f, 0.01f, 0, 0, 0, 1, sprites, 1, 8, -0.05f, true);
		this.gravity = 3.0e-6f;
		this.friction = 0.8f;
		this.xd = dx;
		this.yd = dy + level.random.nextFloat() / 500f;
		this.zd = dz;
		this.rCol = 1;
		this.gCol = 1;
		this.bCol = 1;
	}

	@Override
	public void tick() {
		super.tick();
		float progress = Mth.clamp((float) this.age / (float) this.lifetime, 0, 1);
		this.alpha = this.lifetime == 0 || this.age >= this.lifetime ? 0 : 1 - progress * progress * progress;
	}

	@Override
	public void setSpriteFromAge(SpriteSet sprite) {
		float progress = Mth.clamp((float) this.age / (float) this.lifetime * 4f, 0, 1);
		float inv = 1 - progress;
		float spriteProgress = 1 - inv * inv * inv * inv;
		if (!this.removed)
			this.setSprite(sprite.get((int) Math.floor(spriteProgress * this.lifetime), this.lifetime));
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		float f = (this.age + scaleFactor) / (float) this.lifetime * 32.0F;
		return this.quadSize * Mth.lerp(f, 0.95f, 1.0f);
	}

	@Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

	public static class Provider implements ParticleProvider<FlakSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(FlakSmokeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			FlakSmokeParticle particle = new FlakSmokeParticle(level, x, y, z, dx, dy, dz, this.sprites);
			particle.scale(3.0f);
			particle.setSize(0.25f, 0.25f);
			particle.setLifetime(data.lifetime());
			return particle;
		}
	}

}
