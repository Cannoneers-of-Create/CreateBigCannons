package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class CannonCastBlockEntityRenderer extends SafeTileEntityRenderer<CannonCastBlockEntity> {

	public CannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();
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
		}
		
		ms.popPose();
	}

}
