package rbasamoyai.createbigcannons.munitions.autocannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class AutocannonProjectileRenderer<T extends AbstractAutocannonProjectile> extends EntityRenderer<T> {

    private static final RenderType NORMAL = RenderType.entityCutoutNoCull(CreateBigCannons.resource("textures/entity/shrapnel.png"));
    private static final RenderType TRACER = RenderType.entityCutoutNoCull(CreateBigCannons.resource("textures/entity/tracer.png"));

    public AutocannonProjectileRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));

        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f pose = lastPose.pose();
        Matrix3f normal = lastPose.normal();
        VertexConsumer builder = buffers.getBuffer(entity.isTracer() ? TRACER : NORMAL);
        if (entity.isTracer()) packedLight = LightTexture.FULL_BRIGHT;

        vertex(builder, pose, normal, packedLight, 0.0f, 0, 0, 1);
        vertex(builder, pose, normal, packedLight, 1.0f, 0, 1, 1);
        vertex(builder, pose, normal, packedLight, 1.0f, 1, 1, 0);
        vertex(builder, pose, normal, packedLight, 0.0f, 1, 0, 0);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustrum, double x, double y, double z) {
        return entity.isTracer() || super.shouldRender(entity, frustrum, x, y, z);
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

    @Override public ResourceLocation getTextureLocation(T entity) { return null; }

}
