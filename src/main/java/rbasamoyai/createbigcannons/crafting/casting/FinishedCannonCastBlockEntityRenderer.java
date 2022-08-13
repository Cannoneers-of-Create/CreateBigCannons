package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockPartials;

public class FinishedCannonCastBlockEntityRenderer extends SafeTileEntityRenderer<FinishedCannonCastBlockEntity> {

	public FinishedCannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();
	}
	
	@Override
	protected void renderSafe(FinishedCannonCastBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.isCentralBlock()) return;
		ms.pushPose();
		
		BlockState state = te.getBlockState();
		
		SuperByteBuffer castRender = CachedBufferer.partial(CBCBlockPartials.cannonCastFor(te.getRenderedShape()), state);
		castRender
			.translate(1, 0, 1)
			.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		
		ms.popPose();
	}

}
