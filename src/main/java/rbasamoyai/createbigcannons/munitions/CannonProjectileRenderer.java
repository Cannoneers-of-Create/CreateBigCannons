package rbasamoyai.createbigcannons.munitions;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonProjectileRenderer<T extends AbstractCannonProjectile> extends EntityRenderer<T> {

	public CannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Level level = entity.getLevel();
			poseStack.pushPose();
			BlockPos pos = entity.blockPosition();
			
			Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
			Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks));
			q.mul(q1);
			poseStack.mulPose(q);
			
			poseStack.translate(-0.5d, -0.1d, -0.5d);
			
			BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
			for (RenderType type : RenderType.chunkBufferLayers()) {
				ForgeHooksClient.setRenderType(type);
				brd.getModelRenderer().tesselateBlock(level, brd.getBlockModel(blockState), blockState, pos, poseStack, buffers.getBuffer(type), false, new Random(), blockState.getSeed(pos), OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
			}
			ForgeHooksClient.setRenderType(null);
			
			poseStack.popPose();
			super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
		}
	}
	
	private static final ResourceLocation TEXTURE_INVALID = CreateBigCannons.resource("textures/entity/invalid.png");
	@Override public ResourceLocation getTextureLocation(T projectile) { return TEXTURE_INVALID; }

}
