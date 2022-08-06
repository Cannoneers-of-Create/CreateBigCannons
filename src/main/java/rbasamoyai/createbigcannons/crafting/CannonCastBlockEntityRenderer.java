package rbasamoyai.createbigcannons.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
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
		
		ms.popPose();
	}

}
