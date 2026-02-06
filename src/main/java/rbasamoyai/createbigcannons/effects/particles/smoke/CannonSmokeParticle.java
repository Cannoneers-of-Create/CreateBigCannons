package rbasamoyai.createbigcannons.effects.particles.smoke;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.ParticleWindHandler;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

public class CannonSmokeParticle extends BaseAshSmokeParticle {

	private static final ResourceLocation GRADIENT_LOCATION = CreateBigCannons.resource("textures/particle/cannon_smoke_particle_gradient.png");

	private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.depthMask(true);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
			RenderSystem.setShaderTexture(3, GRADIENT_LOCATION);
			return CBCRenderTypes.CANNON_SMOKE_PARTICLE.createBuilder(tesselator);
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
    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float size = this.getQuadSize(partialTicks);
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int light = this.getLightColor(partialTicks);
        int cannonPower = (int) Math.floor(this.power);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, size, u1, v1, light, cannonPower);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, size, u1, v0, light, cannonPower);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, size, u0, v0, light, cannonPower);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, size, u0, v1, light, cannonPower);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset,
        float yOffset, float quadSize, float u, float v, int packedLight, int cannonPower) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z())
            .setUv(u, v)
            .setOverlay(OverlayTexture.pack(0, cannonPower))
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
            .setLight(packedLight);
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
			if (CBCConfigs.client().useShaderCompatibleGraphics.get()) {
				level.addParticle(new FallbackCannonSmokeParticleData(data), true, x, y, z, dx, dy, dz);
				return null;
			}
			CannonSmokeParticle particle = new CannonSmokeParticle(level, x, y, z, dx, dy, dz, this.sprites, data.power());
			particle.quadSize = data.size();
			particle.friction = data.friction();
			particle.setLifetime(data.lifetime());
			return particle;
		}
	}

}
