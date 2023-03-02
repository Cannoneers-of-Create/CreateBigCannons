package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class BigCannonProjectileRenderer<T extends AbstractBigCannonProjectile> extends EntityRenderer<T> {

	public BigCannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();
			
			Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
			Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks) - 90.0f);
			q.mul(q1);

			poseStack.translate(0.0d, 0.4d, 0.0d);
			poseStack.mulPose(q);

			Minecraft.getInstance().getItemRenderer()
					.renderStatic(new ItemStack(blockState.getBlock()), ItemTransforms.TransformType.NONE, packedLight,
							OverlayTexture.NO_OVERLAY, poseStack, buffers, 0);
			
			poseStack.popPose();
			super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
		}
	}

	@Override public ResourceLocation getTextureLocation(T projectile) { return null; }

}
