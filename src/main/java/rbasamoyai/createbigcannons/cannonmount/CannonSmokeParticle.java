package rbasamoyai.createbigcannons.cannonmount;

import com.mojang.math.Vector3f;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class CannonSmokeParticle extends BaseAshSmokeParticle {

	private float growthDecay = 0.95f;
	private float baseGrowth = 0.5f;
	private final float maxSize;
	
	private final float rStart;
	private final float gStart;
	private final float bStart;
	private final float rFinal;
	private final float gFinal;
	private final float bFinal;
	private final int shiftTime;
	
	CannonSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float size, SpriteSet sprites, float rs, float gs, float bs, float rf, float gf, float bf, int shiftTime) {
		super(level, x, y, z, 0.1f, 0.1f, 0.1f, dx, dy, dz, 1, sprites, 0, 8, -0.05f, true);
		
		this.friction = 0.99f;
		
		this.lifetime = 200;
		
		this.rStart = rs;
		this.gStart = gs;
		this.bStart = bs;
		
		this.rCol = this.rStart;
		this.gCol = this.gStart;
		this.bCol = this.bStart;
		
		this.rFinal = rf;
		this.gFinal = gf;
		this.bFinal = bf;
		
		this.shiftTime = shiftTime;
		
		this.maxSize = size;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		this.alpha = this.lifetime == 0 || this.age >= this.lifetime ? 0 : 1 - (float) this.age / (float) this.lifetime;
		
		if (this.quadSize < this.maxSize) this.quadSize *= 1 + this.baseGrowth;
		this.baseGrowth *= this.growthDecay;
		
		float progress = this.shiftTime == 0 || this.age >= this.shiftTime ? 1 : (float) this.age / (float) this.shiftTime;
		this.rCol = Mth.lerp(progress, this.rStart, this.rFinal);
		this.gCol = Mth.lerp(progress, this.gStart, this.gFinal);
		this.bCol = Mth.lerp(progress, this.bStart, this.bFinal);
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
			Vector3f startColor = data.startColor();
			Vector3f endColor = data.endColor();
			return new CannonSmokeParticle(level, x, y, z, dx, dy, dz, data.scale(), this.sprites, startColor.x(), startColor.y(), startColor.z(), endColor.x(), endColor.y(), endColor.z(), data.shiftTime());
		}
		
	}

}
