package rbasamoyai.createbigcannons.effects.particles.impacts;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

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
import rbasamoyai.createbigcannons.index.CBCVertexFormatElements;
import rbasamoyai.createbigcannons.mixin.client.BufferBuilderAccessor;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.remix.LightingRemix;

public class SplinterParticle extends CBCBlockParticle {
	protected TextureAtlasSprite sprite1;
	protected float yaw;
	protected float pitch;

	private static final ParticleRenderType RENDER_TYPE = new ParticleRenderType() {
		@Override
		public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.depthMask(true);
			RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
			RenderSystem.setShaderTexture(3, TextureAtlas.LOCATION_PARTICLES);
			Minecraft minecraft = Minecraft.getInstance();
			if (minecraft.level != null)
				LightingRemix.reapplyLevelLighting(minecraft.level.effects().constantAmbientLight());
			return CBCRenderTypes.SPLINTER_PARTICLE.createBuilder(tesselator);
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
		int i = Minecraft.getInstance().getBlockColors().getColor(state, level, BlockPos.containing(x, y, z), 0);
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
        Quaternionf quaternion = new Quaternionf();
        quaternion.mul(Axis.ZP.rotation(this.roll));
        quaternion.mul(Axis.YP.rotation(this.yaw));
        quaternion.mul(Axis.XP.rotation(this.pitch));
        Vec3 vec31 = new Vec3(this.xd, this.yd, this.zd).normalize();
        Vec3 vec32 = new Vec3(1, 0, 0);
        double dot = vec32.dot(vec31);
        if (Math.abs(dot + 1) < 1e-4d) { // anti-parallel
            quaternion.mul(Axis.YP.rotation(Mth.PI));
        } else {
            Vec3 cross = vec32.cross(vec31);
            quaternion.mul(new Quaternionf((float) cross.x, (float) cross.y, (float) cross.z, 1f + (float) dot));
        }
        quaternion.normalize();
        this.renderRotatedQuad(buffer, renderInfo, quaternion, partialTicks);
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        if (!(buffer instanceof BufferBuilder builder))
            return;

        float size = this.getQuadSize(partialTicks);
        float blockU0 = this.sprite.getU0();
        float blockU1 = this.sprite.getU1();
        float blockV0 = this.sprite.getV0();
        float blockV1 = this.sprite.getV1();
        float particleU0 = this.sprite1.getU0();
        float particleU1 = this.sprite1.getU1();
        float particleV0 = this.sprite1.getV0();
        float particleV1 = this.sprite1.getV1();
        int light = this.getLightColor(partialTicks);
        Vector3f normal = quaternion.transform(new Vector3f(0, 0, -1));

        this.renderVertex(builder, quaternion, x, y, z, 1.0F, -1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU1, particleV1, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, 1.0F, 1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU1, particleV0, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, -1.0F, 1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU0, particleV0, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, -1.0F, -1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU0, particleV1, light, normal);

        normal.mul(-1);

        this.renderVertex(builder, quaternion, x, y, z, 1.0F, -1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU1, particleV1, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, -1.0F, -1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU0, particleV1, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, -1.0F, 1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU0, particleV0, light, normal);
        this.renderVertex(builder, quaternion, x, y, z, 1.0F, 1.0F, size, blockU0, blockV0, blockU1, blockV1, particleU1, particleV0, light, normal);
    }

    private void renderVertex(BufferBuilder buffer, Quaternionf quaternion, float x, float y, float z, float xOffset,
                              float yOffset, float quadSize, float blockU0, float blockV0, float blockU1, float blockV1,
                              float particleU, float particleV, int packedLight, Vector3f normal) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);

        // Because we can't have nice things in 1.21.1
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z());
        BufferBuilderAccessor bufferAcc = (BufferBuilderAccessor) buffer;
        long i1 = bufferAcc.callBeginElement(CBCVertexFormatElements.BLOCK_UV.element);
        if (i1 != -1L) {
            MemoryUtil.memPutFloat(i1, blockU0);
            MemoryUtil.memPutFloat(i1 + 4L, blockV0);
            MemoryUtil.memPutFloat(i1 + 8L, blockU1);
            MemoryUtil.memPutFloat(i1 + 12L, blockV1);
        }

        buffer
            .setUv(particleU, particleV)
            .setColor(this.rCol, this.gCol, this.bCol, this.alpha)
            .setLight(packedLight)
            .setNormal(normal.x(), normal.y(), normal.z());
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
			if (CBCConfigs.client().useShaderCompatibleGraphics.get()) {
				level.addParticle(new CBCBlockParticleData(blockstate), x, y, z, xSpeed, ySpeed, zSpeed);
				return null;
			}
			SplinterParticle particle = new SplinterParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, blockstate);
			IndexPlatform.updateSprite(particle, blockstate, BlockPos.containing(x, y, z));
			particle.setSecondarySprite(this.sprites.get(level.getRandom()));
			return particle;
		}
	}

}
