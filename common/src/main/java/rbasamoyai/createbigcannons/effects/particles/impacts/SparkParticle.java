package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class SparkParticle extends Particle {

	private static final ResourceLocation COLOR_LOCATION = CreateBigCannons.resource("textures/entity/color.png");
	private static final RenderType COLOR = RenderType.entityTranslucentCull(COLOR_LOCATION);

	SparkParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, float r, float g, float b) {
		super(level, x, y, z);
		this.xd = dx;
		this.yd = dy;
		this.zd = dz;
		this.rCol = r;
		this.gCol = g;
		this.bCol = b;
		this.gravity = 0.5f;
		this.friction = 0.8f;
	}

	private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
		@Override
		public void begin(BufferBuilder builder, TextureManager textureManager) {
			RenderSystem.depthMask(true);
			RenderSystem.disableBlend();
			RenderSystem.setShader(GameRenderer::getRendertypeEntityTranslucentCullShader);
			COLOR.setupRenderState();
			builder.begin(COLOR.mode(), COLOR.format());
		}

		@Override
		public void end(Tesselator tesselator) {
			tesselator.end();
		}

		@Override public String toString() { return "SPARK"; }
	};

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Vec3 vec3 = renderInfo.getPosition();
		double dx = this.x - this.xo;
		double dy = this.y - this.yo;
		double dz = this.z - this.zo;
		float x1 = (float)(this.xo + partialTicks * dx - vec3.x());
		float y1 = (float)(this.yo + partialTicks * dy - vec3.y());
		float z1 = (float)(this.zo + partialTicks * dz - vec3.z());

		Vec3 vel = new Vec3(dx, dy, dz);
		Matrix3f orient = CBCUtils.mat3x3fFacing(vel.normalize());

		float width = 1 / 16f;
		float length = -Math.min((float) vel.length(), 1);
		int p = this.getLightColor(partialTicks);
		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-width, 0, 0), new Vector3f(-width, 0, length), new Vector3f(width, 0, length), new Vector3f(width, 0, 0),
			new Vector3f(-width, 0, 0), new Vector3f(width, 0, 0), new Vector3f(width, 0, length), new Vector3f(-width, 0, length),
			new Vector3f(0, -width, 0), new Vector3f(0, -width, length), new Vector3f(0, width, length), new Vector3f(0, width, 0),
			new Vector3f(0, -width, 0), new Vector3f(0, width, 0), new Vector3f(0, width, length), new Vector3f(0, -width, length),
		};

		for(int vert = 0; vert < 16; ++vert) {
			Vector3f vector3f2 = vector3fs[vert];
			vector3f2.transform(orient);
			buffer.vertex(vector3f2.x() + x1, vector3f2.y() + y1, vector3f2.z() + z1)
				.color(this.rCol, this.gCol, this.bCol, this.alpha)
				.uv(0, 0)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(p)
				.normal(0, 1, 0)
				.endVertex();
		}
	}

	@Override public ParticleRenderType getRenderType() { return RENDER_TYPE; }

	@Override
	public void tick() {
		if (this.onGround) {
			this.remove();
			return;
		}
		this.rCol *= 0.99f;
		this.gCol *= 0.98f;
		this.bCol *= 0.97f;
		super.tick();
	}

	@Override
	public int getLightColor(float partialTick) {
		float progress = 1 - Mth.clamp((this.age + partialTick) / (float) this.lifetime, 0, 1);
		float brightness = Math.max(0.75f, progress * progress * progress * progress);

		int i = super.getLightColor(partialTick);
		int j = i & 0xFF;
		int k = i >> 16 & 0xFF;
		j += (int)(brightness * 240f);
		if (j > 240)
			j = 240;
		return j | k << 16;
	}

	public static class Provider implements ParticleProvider<SparkParticleData> {
		@Override
		public Particle createParticle(SparkParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			Vector3f color = type.color();
			SparkParticle particle = new SparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, color.x(), color.y(), color.z());
			int lifetime = 12 + level.random.nextInt(12);
			particle.setLifetime(lifetime);
			return particle;
		}
	}

}
