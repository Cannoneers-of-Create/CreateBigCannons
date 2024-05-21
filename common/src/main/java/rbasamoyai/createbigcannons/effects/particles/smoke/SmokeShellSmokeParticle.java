package rbasamoyai.createbigcannons.effects.particles.smoke;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class SmokeShellSmokeParticle extends BaseAshSmokeParticle {

    private float baseGrowth = 0.5f;
	private final float maxSize;

	SmokeShellSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float size, SpriteSet sprites) {
		super(level, x, y, z, 0.1f, 0.1f, 0.1f, dx, dy, dz, 1, sprites, 0, 8, -0.05f, true);
		this.friction = 0.95f;
		this.gravity = 0;
		this.lifetime = 200;
		this.maxSize = size;
	}

	@Override
	public void tick() {
		super.tick();

		this.alpha = this.lifetime == 0 || this.age >= this.lifetime ? 0 : 1 - (float) this.age / (float) this.lifetime;

		if (this.quadSize < this.maxSize) this.quadSize *= 1 + this.baseGrowth;
        this.baseGrowth *= 0.95f;

		float progress = this.age >= 60 ? 1 : (float) this.age / 60f;
		this.rCol = Mth.lerp(progress, 0.85f, 0.75f);
		this.gCol = Mth.lerp(progress, 0.85f, 0.75f);
		this.bCol = Mth.lerp(progress, 0.85f, 0.75f);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Provider implements ParticleProvider<SmokeShellSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SmokeShellSmokeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			return new SmokeShellSmokeParticle(level, x, y, z, dx, dy, dz, data.scale(), this.sprites);
		}
	}

}
