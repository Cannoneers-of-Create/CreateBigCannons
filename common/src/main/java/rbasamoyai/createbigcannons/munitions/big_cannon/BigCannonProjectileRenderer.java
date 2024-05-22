package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class BigCannonProjectileRenderer<T extends AbstractBigCannonProjectile<?>> extends EntityRenderer<T> {

	public BigCannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		boolean isTracer = entity.hasTracer();
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();

			Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
			Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks) - 90.0f);
			q.mul(q1);

			poseStack.translate(0.0d, 0.4d, 0.0d);
			poseStack.mulPose(q);

			Minecraft.getInstance().getItemRenderer()
					.renderStatic(new ItemStack(blockState.getBlock()), ItemTransforms.TransformType.NONE, isTracer ? LightTexture.FULL_BRIGHT : packedLight,
							OverlayTexture.NO_OVERLAY, poseStack, buffers, 0);

			poseStack.popPose();
		}
		if (isTracer) {
			int frame = (int)((entity.getId() + entity.level.getGameTime()) % 4L);
			ResourceLocation textureLoc = CreateBigCannons.resource(String.format("textures/entity/tracer_glow%d.png", frame));
			RenderType renderType = RenderType.entityCutoutNoCull(textureLoc);

			poseStack.pushPose();
			poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
			poseStack.scale(1.5f, 1.5f, 1.5f);

			PoseStack.Pose lastPose = poseStack.last();
			Matrix4f pose = lastPose.pose();
			Matrix3f normal = lastPose.normal();
			VertexConsumer builder = buffers.getBuffer(renderType);

			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, 0.0f, 0, 0, 1);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, 1.0f, 0, 1, 1);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, 1.0f, 1, 1, 0);
			vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, 0.0f, 1, 0, 0);

			poseStack.popPose();
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override public ResourceLocation getTextureLocation(T projectile) { return null; }

	@Override
	public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
		return entity.hasTracer() || super.shouldRender(entity, camera, camX, camY, camZ);
	}

	private static void vertex(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int packedLight, float x, int y, int u, int v) {
		builder.vertex(pose, x - 0.5f, (float) y - 0.25f, 0.0f)
			.color(255, 255, 255, 255)
			.uv((float) u, (float) v)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(packedLight)
			.normal(normal, 0.0f, 1.0f, 0.0f)
			.endVertex();
	}

}
