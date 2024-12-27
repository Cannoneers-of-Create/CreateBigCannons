package rbasamoyai.createbigcannons.munitions.autocannon;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class AutocannonProjectileRenderer<T extends AbstractAutocannonProjectile> extends EntityRenderer<T> {

	private static final ResourceLocation SHRAPNEL_LOCATION = CreateBigCannons.resource("textures/entity/shrapnel.png");
	private static final ResourceLocation COLOR_LOCATION = CreateBigCannons.resource("textures/entity/color.png");
	private static final RenderType SHRAPNEL = RenderType.entityCutoutNoCull(SHRAPNEL_LOCATION);
	private static final RenderType COLOR = RenderType.entityTranslucentCull(COLOR_LOCATION);

    public AutocannonProjectileRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		if (entity.isTracer()) {
			Vec3 previous = new Vec3(entity.xOld, entity.yOld, entity.zOld);
			Vec3 diff = entity.position().subtract(previous);
			double dlSqr = diff.lengthSqr();
			boolean isFastButNotTeleported = 1e-4d <= dlSqr && dlSqr <= entity.getDeltaMovement().lengthSqr() * 4;
			double diffLength = isFastButNotTeleported ? diff.length() : 0;
			double displacement = entity.getTotalDisplacement() - diffLength * (1 - partialTicks);
			float length = (float) Math.min(diffLength, displacement);

			Vec3 vel = entity.getOrientation();
			if (vel.lengthSqr() < 1e-4d)
				vel = new Vec3(0, -1, 0);
			poseStack.pushPose();
			if (vel.horizontalDistanceSqr() > 1e-4d && Math.abs(vel.y) > 1e-2d) {
				Vec3 horizontal = new Vec3(vel.x, 0, vel.z).normalize();
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize().reverse(), horizontal));
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(horizontal, new Vec3(0, 0, -1)));
			} else {
				poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize(), new Vec3(0, 0, -1)));
			}

			PoseStack.Pose lastPose = poseStack.last();
			Matrix4f pose = lastPose.pose();
			Matrix3f normal = lastPose.normal();

			// TODO: config tracer color per projectile?
			VertexConsumer vcons = buffers.getBuffer(COLOR);
			float thickness = entity.getAutocannonRoundType() == AutocannonAmmoType.MACHINE_GUN ? 1 / 32f : 2 / 32f;
			renderBox(vcons, pose, normal, 255, 216, 0, length, thickness);
			renderBoxInverted(vcons, pose, normal, 255, 80, 0, length, thickness * 1.5f);

			poseStack.popPose();
		} else {
			poseStack.pushPose();
			poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
			float scale = entity.getAutocannonRoundType() == AutocannonAmmoType.MACHINE_GUN ? 0.5f : 1f;
			poseStack.scale(scale, scale, scale);

			PoseStack.Pose lastPose = poseStack.last();
			Matrix4f pose = lastPose.pose();
			Matrix3f normal = lastPose.normal();
			VertexConsumer builder = buffers.getBuffer(SHRAPNEL);

			vertexShrapnel(builder, pose, normal, packedLight, -0.5f, -0.5f, 0, 1);
			vertexShrapnel(builder, pose, normal, packedLight,  0.5f, -0.5f, 1, 1);
			vertexShrapnel(builder, pose, normal, packedLight,  0.5f,  0.5f, 1, 0);
			vertexShrapnel(builder, pose, normal, packedLight, -0.5f,  0.5f, 0, 0);

			poseStack.popPose();
		}
        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustrum, double x, double y, double z) {
        return entity.isTracer() || super.shouldRender(entity, frustrum, x, y, z);
    }

	private static void renderBox(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g, int b, float length, float thickness) {
		float x1 = -thickness;
		float y1 = -thickness;
		float z1 = -thickness - length;
		float x2 = thickness;
		float y2 = thickness;
		float z2 = thickness;

		// Front
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);

		// Right
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);

		// Back
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);

		// Left
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);

		// Down
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);

		// Up
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);
	}

	private static void renderBoxInverted(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g, int b, float length, float thickness) {
		float x1 = -thickness;
		float y1 = -thickness;
		float z1 = -thickness - length;
		float x2 = thickness;
		float y2 = thickness;
		float z2 = thickness;

		// Front
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);

		// Right
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);

		// Back
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);

		// Left
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);

		// Down
		vertex(builder, pose, normal, r, g, b, x2, y1, z2);
		vertex(builder, pose, normal, r, g, b, x2, y1, z1);
		vertex(builder, pose, normal, r, g, b, x1, y1, z1);
		vertex(builder, pose, normal, r, g, b, x1, y1, z2);

		// Up
		vertex(builder, pose, normal, r, g, b, x1, y2, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z1);
		vertex(builder, pose, normal, r, g, b, x2, y2, z2);
		vertex(builder, pose, normal, r, g, b, x1, y2, z2);
	}

    private static void vertex(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g, int b, float x, float y, float z) {
        builder.vertex(pose, x, y, z)
			.color(r, g, b, 255)
			.uv(0, 0)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(LightTexture.FULL_BRIGHT)
			.normal(normal, 0, 1, 0)
            .endVertex();
    }

	private static void vertexShrapnel(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int packedLight, float x, float y, int u, int v) {
		builder.vertex(pose, x, y, 0.0f)
			.color(255, 255, 255, 255)
			.uv((float) u, (float) v)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(packedLight)
			.normal(normal, 0.0f, 1.0f, 0.0f)
			.endVertex();
	}

    @Override public ResourceLocation getTextureLocation(T entity) { return null; }

}
