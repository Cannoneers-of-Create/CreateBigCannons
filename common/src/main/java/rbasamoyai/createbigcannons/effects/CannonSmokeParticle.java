package rbasamoyai.createbigcannons.effects;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class CannonSmokeParticle extends BaseAshSmokeParticle {

    private float baseGrowth = 0.5f;
	private final float maxSize;
	private final int shiftTime;
	private final int startShiftTime;

	CannonSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float size, SpriteSet sprites, int shiftTime, int startShiftTime, float friction) {
		super(level, x, y, z, 0.1f, 0.1f, 0.1f, dx, dy, dz, 1, sprites, 0, 8, -0.05f, true);
        this.startShiftTime = startShiftTime;
        this.friction = friction;
		this.gravity = 0;
		this.lifetime = 200;
		this.shiftTime = shiftTime;
		this.maxSize = size;
	}

	@Override
	public void tick() {
		super.tick();

		this.alpha = this.lifetime == 0 || this.age >= this.lifetime ? 0 : 1 - (float) this.age / (float) this.lifetime;

		if (this.quadSize < this.maxSize) this.quadSize *= 1 + this.baseGrowth;
        this.baseGrowth *= 0.95f;

		int shiftAge = this.age - this.startShiftTime;
		float progress;
		if (this.shiftTime == 0) {
			progress = shiftAge < 0 ? 0 : 1;
		} else {
			progress = (float) shiftAge / (float) this.shiftTime;
		}
		if (progress < 0.2f) {
			this.rCol = 1f;
			this.gCol = 1f;
			this.bCol = 0.85f;
		} else if (progress < 0.3f) {
			float progress1 = (progress - 0.2f) / 0.1f;
			this.rCol = 1f; //Mth.lerp(progress1, 1f, 1f);
			this.gCol = Mth.lerp(progress1, 1f, 0.58f);
			this.bCol = Mth.lerp(progress1, 0.85f, 0.2f);
		} else if (progress < 0.8f) {
			float progress1 = (progress - 0.3f) / 0.5f;
			this.rCol = Mth.lerp(progress1, 1f, 0.29f);
			this.gCol = Mth.lerp(progress1, 0.58f, 0.27f);
			this.bCol = Mth.lerp(progress1, 0.2f, 0.25f);
		} else {
			this.rCol = 0.29f;
			this.gCol = 0.27f;
			this.bCol = 0.25f;
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Provider implements ParticleProvider<CannonSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(CannonSmokeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			CannonSmokeParticle particle = new CannonSmokeParticle(level, x, y, z, dx, dy, dz, data.scale(), this.sprites, data.shiftTime(), data.startShiftTime(), data.friction());
			particle.setLifetime(data.lifetime());
			return particle;
		}

	}

}
