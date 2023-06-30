package rbasamoyai.createbigcannons.munitions.autocannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class AutocannonProjectileRenderer<T extends AbstractAutocannonProjectile> extends EntityRenderer<T> {

    public AutocannonProjectileRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		Vec3 previous = new Vec3(entity.xo, entity.yo, entity.zo);
		Vec3 secondPrevious = entity.getSecondPreviousPos();
		Vec3 dir = entity.position().subtract(secondPrevious);
		boolean flag = dir.lengthSqr() < 1e-4d;

		float yaw = flag ? entity.getViewYRot(partialTicks) : (float) Math.atan2(dir.x, dir.z) * Mth.RAD_TO_DEG;
		float pitch = flag ? entity.getViewXRot(partialTicks) : (float) Math.atan2(dir.y, dir.x) * Mth.RAD_TO_DEG;
        Quaternion q = Vector3f.YP.rotationDegrees(yaw + 180.0f);
        Quaternion q1 = Vector3f.XP.rotationDegrees(pitch);
        q.mul(q1);

        poseStack.pushPose();
		poseStack.mulPose(q);
		poseStack.translate(0, entity.getBbHeight() / 2, 0);

		float len = (float) dir.length();
		PoseStack.Pose lastPose = poseStack.last();
		Matrix4f pose = lastPose.pose();
		Matrix3f normal = lastPose.normal();

		// TODO: config tracer color per projectile?
		VertexConsumer vcons = buffers.getBuffer(RenderType.entityCutout(CreateBigCannons.resource("textures/entity/tracer.png")));
		renderBox(vcons, pose, normal, 255, 216, 0, len, 1 / 16f);
		renderBoxInverted(vcons, pose, normal, 255, 80, 0, len, 1.5f / 16f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustrum, double x, double y, double z) {
        return entity.isTracer();
    }

	private static void renderBox(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g,
								  int b, float length, float thickness) {
		float x1 = -thickness;
		float y1 = -thickness;
		float z1 = -thickness;
		float x2 = thickness;
		float y2 = thickness;
		float z2 = length + thickness;

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

	private static void renderBoxInverted(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g,
								  int b, float length, float thickness) {
		float x1 = -thickness;
		float y1 = -thickness;
		float z1 = -thickness;
		float x2 = thickness;
		float y2 = thickness;
		float z2 = length + thickness;

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

    private static void vertex(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int r, int g, int b,
							   float x, float y, float z) {
        builder.vertex(pose, x, y, z)
                .color(r, g, b, 255)
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normal, 0, 1, 0)
                .endVertex();
    }

    @Override public ResourceLocation getTextureLocation(T entity) { return null; }

}
