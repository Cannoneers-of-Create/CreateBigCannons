package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.remix.LightingRemix;

public class SplinterParticle extends CBCBlockParticle {
	protected TextureAtlasSprite sprite1;
	protected float yaw;
	protected float pitch;

	private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
		@Override
		public void begin(BufferBuilder builder, TextureManager textureManager) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.depthMask(true);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
			RenderSystem.setShaderTexture(3, TextureAtlas.LOCATION_PARTICLES);
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level != null)
				LightingRemix.reapplyLevelLighting(minecraft.level.effects().constantAmbientLight());
			CBCRenderTypes.SPLINTER_PARTICLE.setRenderTypeForBuilder(builder);
		}

		@Override
		public void end(Tesselator tesselator) {
			tesselator.end();
		}

		public String toString() { return "SPLINTER"; }
	};

	SplinterParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, state);
		this.roll = this.random.nextFloat() * Mth.PI;
		this.yaw = this.random.nextFloat() * - 0.5f * Mth.HALF_PI;
		this.pitch = 0;//level.random.nextFloat() * - 0.5f * Mth.HALF_PI;
		this.gravity = 1f;
		this.friction = 0.99f;
		this.quadSize = 0.3f + this.random.nextFloat() * 0.1f;
		this.setSize(0.1f, 0.1f);
		this.setLifetime(30 + this.random.nextInt(8));
		int i = Minecraft.getInstance().getBlockColors().getColor(state, level, new BlockPos(x, y, z), 0);
		this.rCol = (float)(i >> 16 & 0xFF) / 255.0F;
		this.gCol = (float)(i >> 8 & 0xFF) / 255.0F;
		this.bCol = (float)(i & 0xFF) / 255.0F;
	}

	@Override public ParticleRenderType getRenderType() { return RENDER_TYPE; }

	protected void setSecondarySprite(TextureAtlasSprite sprite) {
		this.sprite1 = sprite;
	}

	@Override
	public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
		Vec3 vec3 = renderInfo.getPosition();
		float x1 = (float)(this.xo + partialTicks * (this.x - this.xo) - vec3.x());
		float y1 = (float)(this.yo + partialTicks * (this.y - this.yo) - vec3.y());
		float z1 = (float)(this.zo + partialTicks * (this.z - this.zo) - vec3.z());

		Quaternion quaternion = Vector3f.ZP.rotation(this.roll);
		quaternion.mul(Vector3f.YP.rotation(this.yaw));
		quaternion.mul(Vector3f.XP.rotation(this.pitch));
		Vec3 vec31 = new Vec3(this.xd, this.yd, this.zd).normalize();
		Vec3 vec32 = new Vec3(1, 0, 0);
		double dot = vec32.dot(vec31);
		if (Math.abs(dot + 1) < 1e-4d) { // anti-parallel
			quaternion.mul(Vector3f.YP.rotation(Mth.PI));
		} else {
			Vec3 cross = vec32.cross(vec31);
			quaternion.mul(new Quaternion((float) cross.x, (float) cross.y, (float) cross.z, 1f + (float) dot));
		}
		quaternion.normalize();

		float j = this.getQuadSize(partialTicks);

		Vector3f[] vector3fs = new Vector3f[]{
			new Vector3f(-1f, -1f, 0f), new Vector3f(-1f, 1f, 0f), new Vector3f(1f, 1f, 0f), new Vector3f(1f, -1f, 0f)
		};

		for(int k = 0; k < 4; ++k) {
			Vector3f vector3f2 = vector3fs[k];
			vector3f2.transform(quaternion);
			vector3f2.mul(j);
			vector3f2.add(x1, y1, z1);
		}
		Vector3f normal = new Vector3f(0, 0, -1);
		normal.transform(quaternion);

		float blockU0 = this.sprite.getU0();
		float blockU1 = this.sprite.getU1();
		float blockV0 = this.sprite.getV0();
		float blockV1 = this.sprite.getV1();
		float particleU0 = this.sprite1.getU0();
		float particleU1 = this.sprite1.getU1();
		float particleV0 = this.sprite1.getV0();
		float particleV1 = this.sprite1.getV1();
		int p = this.getLightColor(partialTicks);

		buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU1, particleV1)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU1, particleV0)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU0, particleV0)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU0, particleV1)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();

		normal.mul(-1);

		buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU1, particleV1)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU0, particleV1)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU0, particleV0)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
		buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z())
			.uv(blockU0, blockV0)
			.uv(blockU1, blockV1)
			.uv(particleU1, particleV0)
			.color(this.rCol, this.gCol, this.bCol, this.alpha)
			.uv2(p)
			.normal(normal.x(), normal.y(), normal.z())
			.endVertex();
	}

	@Override
	public void tick() {
		if (this.age >= this.lifetime - 10) {
			this.alpha -= 0.1f;
		}
		super.tick();
	}

	public static class Provider implements ParticleProvider<SplinterParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) { this.sprites = sprites; }

		@Override
		public Particle createParticle(SplinterParticleData type, ClientLevel level, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = type.state();
			if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON))
				return null;
			if (CBCConfigs.CLIENT.useShaderCompatibleGraphics.get()) {
				level.addParticle(new CBCBlockParticleData(blockstate), x, y, z, xSpeed, ySpeed, zSpeed);
				return null;
			}
			SplinterParticle particle = new SplinterParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate);
			IndexPlatform.updateSprite(particle, blockstate, new BlockPos(x, y, z));
			particle.setSecondarySprite(this.sprites.get(level.getRandom()));
			return particle;
		}
	}

}
