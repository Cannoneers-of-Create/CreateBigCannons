package rbasamoyai.createbigcannons.munitions;

import com.jozufozu.flywheel.core.virtual.VirtualEmptyModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonProjectileRenderer<T extends AbstractCannonProjectile> extends EntityRenderer<T> {

	public CannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();
			
			Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
			Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks));
			q.mul(q1);
			poseStack.mulPose(q);
			
			poseStack.translate(-0.5d, -0.1d, -0.5d);
			
			BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
			brd.renderSingleBlock(blockState, poseStack, buffers, packedLight, OverlayTexture.NO_OVERLAY, VirtualEmptyModelData.INSTANCE);
			
			poseStack.popPose();
			super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
		}
	}
	@Override public ResourceLocation getTextureLocation(T projectile) { return null; }

}
