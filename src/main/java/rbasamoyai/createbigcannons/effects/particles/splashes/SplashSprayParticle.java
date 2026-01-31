package rbasamoyai.createbigcannons.effects.particles.splashes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class SplashSprayParticle extends TextureSheetParticle {

	private final float light;

	SplashSprayParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float r, float g, float b, float light) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.rCol = r;
		this.gCol = g;
		this.bCol = b;
		this.gravity = 0.5f;
		this.light = light;
	}

	@Override
	public void tick() {
		if (this.onGround && this.age < this.lifetime - 30) {
			this.age = this.lifetime - 30;
		}
		if (this.age >= this.lifetime - 30 && this.alpha > 0.01F) {
			this.alpha -= 0.03F;
		}
		super.tick();
	}

	@Override
	public int getLightColor(float partialTick) {
		float progress = 1 - Mth.clamp((this.age + partialTick) / (float) this.lifetime, 0, 1);
		float brightness = Math.max(0.5f, progress * progress);

		int i = super.getLightColor(partialTick);
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		j += (int)(brightness * 240f * this.light);
		if (j > 240)
			j = 240;
		return j | k << 16;
	}

	@Override public ParticleRenderType getRenderType() { return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; }

	public static class Provider implements ParticleProvider<SplashSprayParticleData> {
		private final SpriteSet sprites;
		public Provider(SpriteSet sprites) { this.sprites = sprites; }

		@Override
		public Particle createParticle(SplashSprayParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			SplashSprayParticle particle = new SplashSprayParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.r(), type.g(), type.b(), type.light());
			particle.setLifetime(type.lifetime());
			float f = type.size();
			particle.quadSize = f;
			particle.setSize(f * 0.5f, f * 0.5f);
			particle.pickSprite(this.sprites);
			particle.setAlpha(0.95f);
			return particle;
		}
	}

}
