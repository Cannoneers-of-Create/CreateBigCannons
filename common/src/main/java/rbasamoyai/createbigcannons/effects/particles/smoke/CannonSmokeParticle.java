package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.effects.particles.ParticleWindHandler;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

public class CannonSmokeParticle extends BaseAshSmokeParticle {

	private static final ResourceLocation GRADIENT_LOCATION = CreateBigCannons.resource("textures/particle/cannon_smoke_particle_gradient.png");

	private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
		@Override
		public void begin(BufferBuilder builder, TextureManager textureManager) {
			RenderSystem.depthMask(true);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.setShaderTexture(3, GRADIENT_LOCATION);
			CBCRenderTypes.CANNON_SMOKE_PARTICLE.setRenderTypeForBuilder(builder);
		}

		@Override
		public void end(Tesselator tesselator) {
			tesselator.end();
		}

		@Override public String toString() { return "CANNON_SMOKE"; }
	};

	private final float power;
	private final Vec3 wind = ParticleWindHandler.getWindForce(0);

	CannonSmokeParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites, float power) {
		super(level, x, y, z, 0.1f, 0.1f, 0.1f, dx, dy, dz, 1, sprites, 1, 8, -0.05f, true);
		this.power = power;
		this.rCol = 1;
		this.gCol = 1;
		this.bCol = 1;
	}

	@Override
	public void tick() {
		super.tick();
		float f = this.onGround ? 1 : 0.5f;
		this.move(this.wind.x * f, this.wind.y, this.wind.z * f);
		float progress = Mth.clamp((float) this.age / (float) this.lifetime, 0, 1);
		this.alpha = this.lifetime == 0 || this.age >= this.lifetime ? 0 : 1 - progress * progress;
	}

	@Override
	public void setSpriteFromAge(SpriteSet sprite) {
		float progress = Mth.clamp((float) this.age / (float) this.lifetime * 1.5f, 0, 1);
		float inv = 1 - progress;
		float spriteProgress = 1 - inv * inv * inv * inv;
		if (!this.removed)
			this.setSprite(sprite.get((int) Math.floor(spriteProgress * this.lifetime), this.lifetime));
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		float f = (this.age + scaleFactor) / (float) this.lifetime * 32.0F;
		return this.quadSize * Mth.lerp(f, 0.9f, 1.0f);
	}

	@Override public ParticleRenderType getRenderType() { return RENDER_TYPE; }

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Vec3 vec3 = renderInfo.getPosition();
		float f = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
		float g = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
		float h = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
		Quaternion quaternion;
		if (this.roll == 0.0F) {
			quaternion = renderInfo.rotation();
		} else {
			quaternion = new Quaternion(renderInfo.rotation());
			float i = Mth.lerp(partialTicks, this.oRoll, this.roll);
			quaternion.mul(Vector3f.ZP.rotation(i));
		}

		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)
		};
		float j = this.getQuadSize(partialTicks);

		for(int k = 0; k < 4; ++k) {
			Vector3f vector3f2 = vector3fs[k];
			vector3f2.transform(quaternion);
			vector3f2.mul(j);
			vector3f2.add(f, g, h);
		}

		float l = this.getU0();
		float m = this.getU1();
		float n = this.getV0();
		float o = this.getV1();
		int p = this.getLightColor(partialTicks);
		int cannonPower = (int) Math.floor(this.power);
		buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
			.uv(m, o)
			.overlayCoords(0, cannonPower)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
			.uv(m, n)
			.overlayCoords(0, cannonPower)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
			.uv(l, n)
			.overlayCoords(0, cannonPower)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
		buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
			.uv(l, o)
			.overlayCoords(0, cannonPower)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.endVertex();
	}

	@Override
	public int getLightColor(float partialTick) {
		float progress = 1 - Mth.clamp((this.age + partialTick) / (float) this.lifetime * 1.5f, 0, 1);
		float brightness = progress * progress * progress * progress;

		int i = super.getLightColor(partialTick);
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		j += (int)(brightness * 240f);
		if (j > 240)
			j = 240;
		return j | k << 16;
	}

	public static class Provider implements ParticleProvider<CannonSmokeParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(CannonSmokeParticleData data, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
			CannonSmokeParticle particle = new CannonSmokeParticle(level, x, y, z, dx, dy, dz, this.sprites, data.power());
			particle.quadSize = data.size();
			particle.friction = data.friction();
			particle.setLifetime(data.lifetime());
			return particle;
		}
	}

}
