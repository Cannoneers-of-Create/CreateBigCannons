package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.item.ItemDisplayContext;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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

			Quaternionf q = Axis.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
			Quaternionf q1 = Axis.XP.rotationDegrees(entity.getViewXRot(partialTicks) - 90.0f);
			q.mul(q1);

			poseStack.translate(0.0d, 0.4d, 0.0d);
			poseStack.mulPose(q);

			Minecraft.getInstance().getItemRenderer()
					.renderStatic(new ItemStack(blockState.getBlock()), ItemDisplayContext.NONE, packedLight,
							OverlayTexture.NO_OVERLAY, poseStack, buffers, entity.level(), 0);

			poseStack.popPose();
			super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
		}
	}

	@Override public ResourceLocation getTextureLocation(T projectile) { return null; }

}
