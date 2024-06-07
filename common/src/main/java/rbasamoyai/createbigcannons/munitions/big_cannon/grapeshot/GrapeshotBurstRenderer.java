package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelBurstRenderer;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurst.SubProjectile;

public class GrapeshotBurstRenderer extends ShrapnelBurstRenderer<GrapeshotBurst> {

	public GrapeshotBurstRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSubProjectile(SubProjectile subProjectile, float partialTick, PoseStack poseStack,
                                       MultiBufferSource buffers, int packedLight) {
		poseStack.pushPose();
		poseStack.scale(2.0f, 2.0f, 2.0f);
		super.renderSubProjectile(subProjectile, partialTick, poseStack, buffers, packedLight);
		poseStack.popPose();
	}

}
