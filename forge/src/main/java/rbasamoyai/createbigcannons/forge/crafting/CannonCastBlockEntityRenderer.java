package rbasamoyai.createbigcannons.forge.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntityRenderer;

import java.util.List;

public class CannonCastBlockEntityRenderer extends AbstractCannonCastBlockEntityRenderer {

	public CannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) { super(context); }

	@Override
	protected void renderFluidBox(AbstractCannonCastBlockEntity cast, float width, float height, MultiBufferSource buffers, PoseStack stack, int light) {
		if (!(cast instanceof CannonCastBlockEntity castc)) return;
		FluidStack fstack = castc.fluid.getFluid();
		if (!fstack.isEmpty()) FluidRenderer.renderFluidBox(fstack, 0, 0, 0, width, height, width, buffers, stack, light, false);
	}

	@Override
	protected void renderPreview(PoseStack ms, float alpha, VertexConsumer vCons, BlockState state, int light, BlockPos pos) {
		RandomSource rand = RandomSource.create();
		BakedModel model = this.dispatcher.getBlockModel(state);

		for (Direction dir : Direction.values()) {
			rand.setSeed(42L);
			renderQuadList(ms.last(), vCons, 1f, 1f, 1f, alpha, model.getQuads(state, dir, rand), light);
		}

		rand.setSeed(42L);
		renderQuadList(ms.last(), vCons, 1f, 1f, 1f, alpha, model.getQuads(state, null, rand), light);
	}

	// Taken from GhostBlockRenderer.TransparentGhostBlockRenderer
	private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight) {
		for (BakedQuad quad : quads) {
			float f;
			float f1;
			float f2;
			if (quad.isTinted()) {
				f = Mth.clamp(red, 0.0F, 1.0F);
				f1 = Mth.clamp(green, 0.0F, 1.0F);
				f2 = Mth.clamp(blue, 0.0F, 1.0F);
			} else {
				f = 1.0F;
				f1 = 1.0F;
				f2 = 1.0F;
			}

			// TODO: Figure out if this is true or false for the new putBulkData method
			consumer.putBulkData(pose, quad, f, f1, f2, alpha, packedLight, OverlayTexture.NO_OVERLAY, false);
		}
	}

}
