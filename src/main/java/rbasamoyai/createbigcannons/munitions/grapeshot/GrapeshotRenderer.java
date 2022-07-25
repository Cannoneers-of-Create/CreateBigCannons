package rbasamoyai.createbigcannons.munitions.grapeshot;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import rbasamoyai.createbigcannons.munitions.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.shrapnel.ShrapnelRenderer;

public class GrapeshotRenderer extends ShrapnelRenderer {

	public GrapeshotRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(Shrapnel entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		poseStack.pushPose();
		poseStack.scale(2.0f, 2.0f, 2.0f);
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
		poseStack.popPose();
	}

}
