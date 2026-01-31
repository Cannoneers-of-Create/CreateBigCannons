package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Copy of {@link net.minecraft.client.renderer.entity.TntRenderer}. Use only for single-block propellant.
 */
public class PrimedPropellantRenderer extends EntityRenderer<PrimedPropellant> {
	private final BlockRenderDispatcher blockRenderer;

	public PrimedPropellantRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5f;
		this.blockRenderer = context.getBlockRenderDispatcher();
	}

	public void render(PrimedPropellant entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();
		poseStack.translate(0f, 0.5f, 0f);
		int fuze = entity.getFuse();
		if ((float) fuze - partialTicks + 1f < 10f) {
			float f = 1f - ((float) fuze - partialTicks + 1f) / 10f;
			f = Mth.clamp(f, 0f, 1f);
			f *= f;
			f *= f;
			float g = 1f + f * 0.3f;
			poseStack.scale(g, g, g);
		}

		poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
		poseStack.translate(-0.5f, -0.5f, 0.5f);
		poseStack.mulPose(Axis.YP.rotationDegrees(90f));
		TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, entity.getAppearance(), poseStack, buffer, packedLight,  fuze / 5 % 2 == 0);
		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override public ResourceLocation getTextureLocation(PrimedPropellant entity) { return TextureAtlas.LOCATION_BLOCKS; }

}
