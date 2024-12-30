package rbasamoyai.createbigcannons.munitions.big_cannon;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class BigCannonProjectileRenderer<T extends AbstractBigCannonProjectile> extends EntityRenderer<T> {

	public BigCannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5f;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		boolean isTracer = entity.hasTracer();
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Vec3 vel = entity.getOrientation();
			if (vel.lengthSqr() < 1e-4d)
				vel = new Vec3(0, -1, 0);

			poseStack.pushPose();
			if (vel.horizontalDistanceSqr() > 1e-4d && Math.abs(vel.y) > 1e-2d) {
				Vec3 horizontal = new Vec3(vel.x, 0, vel.z).normalize();
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize().reverse(), horizontal));
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(horizontal));
			} else {
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize()));
			}
			poseStack.translate(-0.5, -0.5, -0.5);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, buffers,
				isTracer ? LightTexture.FULL_BRIGHT : packedLight, OverlayTexture.NO_OVERLAY);

			poseStack.popPose();
		}
		if (isTracer) {
			int frame = (int)((entity.getId() + entity.level().getGameTime()) % 4L);
			ResourceLocation textureLoc = CreateBigCannons.resource(String.format("textures/entity/tracer_glow%d.png", frame));
			RenderType renderType = RenderType.entityCutoutNoCull(textureLoc);

			poseStack.pushPose();
			poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			poseStack.scale(1.5f, 1.5f, 1.5f);

			PoseStack.Pose lastPose = poseStack.last();
			Matrix4f pose = lastPose.pose();
			Matrix3f normal = lastPose.normal();
			VertexConsumer builder = buffers.getBuffer(renderType);

			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, -0.5f, -0.5f, 0, 1);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, -0.5f,  0.5f, 0, 0);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT,  0.5f,  0.5f, 1, 0);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT,  0.5f, -0.5f, 1, 1);

			poseStack.popPose();
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override public ResourceLocation getTextureLocation(T projectile) { return null; }

	@Override
	public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
		return entity.hasTracer() || super.shouldRender(entity, camera, camX, camY, camZ);
	}

	private static void vertex(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int packedLight, float x, float y, int u, int v) {
		builder.vertex(pose, x, y, 0.0f)
			.color(255, 255, 255, 255)
			.uv((float) u, (float) v)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(packedLight)
			.normal(normal, 0.0f, 1.0f, 0.0f)
			.endVertex();
	}

}
