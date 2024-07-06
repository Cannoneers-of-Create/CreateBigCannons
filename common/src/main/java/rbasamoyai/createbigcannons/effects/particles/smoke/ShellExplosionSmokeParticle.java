package rbasamoyai.createbigcannons.effects.particles.smoke;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class ShellExplosionSmokeParticle extends BaseAshSmokeParticle {

	ShellExplosionSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
		super(level, x, y, z, 0.01f, 0.01f, 0.01f, 0, 0, 0, 1, sprites, 1, 8, -0.05f, true);
		this.gravity = 0.05f;
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

	@Override
	public int getLightColor(float partialTick) {
		float progress = 1 - Mth.clamp((this.age + partialTick) / (float) this.lifetime * 4f, 0, 1);
		float brightness = progress * progress * progress * progress;

		int i = super.getLightColor(partialTick);
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		j += (int)(brightness * 240f);
		if (j > 240)
			j = 240;
		return j | k << 16;
	}

	@Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

	public static class Provider implements ParticleProvider<ShellExplosionSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(ShellExplosionSmokeParticleData data, ClientLevel level, double x, double y, double z,
									   double dx, double dy, double dz) {
			ShellExplosionSmokeParticle particle = new ShellExplosionSmokeParticle(level, x, y, z, dx, dy, dz, this.sprites);
			particle.scale(data.scale());
			particle.setSize(0.25f, 0.25f);
			particle.setLifetime(data.lifetime());
			return particle;
		}
	}

}
