package rbasamoyai.createbigcannons.munitions.autocannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class AutocannonProjectileRenderer<T extends AbstractAutocannonProjectile> extends EntityRenderer<T> {

    public AutocannonProjectileRenderer(EntityRendererProvider.Context context) { super(context); }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
        Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks));
        q.mul(q1);

        poseStack.pushPose();
        CachedBufferer.partial(CBCBlockPartials.AUTOCANNON_ROUND, Blocks.AIR.defaultBlockState())
                .translate(-0.5, -0.375, -0.5)
                .rotateCentered(q)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(poseStack, buffers.getBuffer(RenderType.cutout()));
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
