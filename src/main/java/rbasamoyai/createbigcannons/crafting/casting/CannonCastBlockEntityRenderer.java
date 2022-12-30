package rbasamoyai.createbigcannons.crafting.casting;

import java.util.List;
import java.util.Random;

import com.jozufozu.flywheel.core.virtual.VirtualEmptyModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class CannonCastBlockEntityRenderer extends SafeTileEntityRenderer<CannonCastBlockEntity> {

	private final BlockRenderDispatcher dispatcher;
	
	public CannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.dispatcher = context.getBlockRenderDispatcher();
	}
	
	@Override
	protected void renderSafe(CannonCastBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.canRenderCastModel()) return;
		BlockState state = te.getBlockState();
		
		ms.pushPose();
		
		SuperByteBuffer castRender = CachedBufferer.partial(CBCBlockPartials.cannonCastFor(te.getRenderedSize()), state);
		castRender
			.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		
		if (te.isController()) {
			LerpedFloat levelLerped = te.getFluidLevel();
			if (levelLerped != null) {
				float level = levelLerped.getValue(partialTicks);
				FluidStack fstack = te.fluid.getFluid();
				
				float boxWidth = 2.875f;
				float height = ((float) te.height - 0.125f) * level;   
				
				if (height > 0.0625f && !fstack.isEmpty()) {
					ms.pushPose();
					ms.translate(-0.9375f, 0.0625f, -0.9375f);
					FluidRenderer.renderFluidBox(fstack, 0, 0, 0, boxWidth, height, boxWidth, buffer, ms, light, false);
					ms.popPose();
				}
			}
			
			LerpedFloat castProgressLerped = te.getCastingLevel();
			if (castProgressLerped != null) {
				float alpha = castProgressLerped.getValue(partialTicks);			
				for (int l = 0; l < te.resultPreview.size(); ++l) {
					BlockState state1 = te.resultPreview.get(l);
					VertexConsumer vCons = buffer.getBuffer(Sheets.translucentItemSheet());
					BakedModel model = this.dispatcher.getBlockModel(state1);
					Random rand = new Random();
					ms.pushPose();
					ms.translate(0, l, 0);
					
					for (Direction dir : Direction.values()) {
						rand.setSeed(42L);
						renderQuadList(ms.last(), vCons, 1f, 1f, 1f, alpha, model.getQuads(state, dir, rand, VirtualEmptyModelData.INSTANCE), light, overlay);
					}
					
					rand.setSeed(42L);
					renderQuadList(ms.last(), vCons, 1f, 1f, 1f, alpha, model.getQuads(state, null, rand, VirtualEmptyModelData.INSTANCE), light, overlay);
					
					ms.popPose();
				}
			}
		}
		
		ms.popPose();
	}
	
	// Taken from GhostBlockRenderer.TransparentGhostBlockRenderer
	private static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay) {
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

			consumer.putBulkData(pose, quad, f, f1, f2, alpha, packedLight, packedOverlay);
		}

	}

}
