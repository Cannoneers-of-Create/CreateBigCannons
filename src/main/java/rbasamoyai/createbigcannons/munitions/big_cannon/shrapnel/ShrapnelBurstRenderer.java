package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurst.SubProjectile;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurstRenderer;

public class ShrapnelBurstRenderer<T extends ShrapnelBurst> extends ProjectileBurstRenderer<T> {

	private static final ResourceLocation TEXTURE_LOCATION = CreateBigCannons.resource("textures/entity/shrapnel.png");
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

	public ShrapnelBurstRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSubProjectile(SubProjectile subProjectile, float partialTick, PoseStack poseStack,
                                       MultiBufferSource buffers, int packedLight) {
		poseStack.pushPose();
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));

		PoseStack.Pose lastPose = poseStack.last();
		Matrix4f pose = lastPose.pose();
		VertexConsumer builder = buffers.getBuffer(RENDER_TYPE);

		vertex(builder, pose, packedLight, 0.0f, 0, 0, 1);
		vertex(builder, pose, packedLight, 1.0f, 0, 1, 1);
		vertex(builder, pose, packedLight, 1.0f, 1, 1, 0);
		vertex(builder, pose, packedLight, 0.0f, 1, 0, 0);

		poseStack.popPose();
	}

    private static void vertex(VertexConsumer builder, Matrix4f pose, int packedLight, float x, float y, int u, int v) {
        builder.addVertex(pose, x, y, 0.0f)
            .setColor(255, 255, 255, 255)
            .setUv((float) u, (float) v)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(packedLight)
            .setNormal(0.0f, 1.0f, 0.0f);
    }

	@Override public ResourceLocation getTextureLocation(ShrapnelBurst entity) { return TEXTURE_LOCATION; }

}
