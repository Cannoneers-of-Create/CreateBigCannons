package rbasamoyai.createbigcannons.crafting.builtup;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.EmptyModelData;

public class LayeredCannonBlockEntityRenderer extends SmartTileEntityRenderer<LayeredCannonBlockEntity> {

	private final BlockRenderDispatcher dispatcher;
	
	public LayeredCannonBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.dispatcher = context.getBlockRenderDispatcher();
	}
	
	@Override
	protected void renderSafe(LayeredCannonBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (be.getLayers().size() < 2) return;
		for (Block block : be.getLayers().values()) {
			BlockState state = block.defaultBlockState();
			if (state.getRenderShape() == RenderShape.MODEL) {
				ms.pushPose();
				
				if (state.hasProperty(BlockStateProperties.FACING) && be.getBlockState().hasProperty(BlockStateProperties.FACING)) {
					state = state.setValue(BlockStateProperties.FACING, be.getBlockState().getValue(BlockStateProperties.FACING));
				}
				
				this.dispatcher.renderSingleBlock(state, ms, buffer, light, overlay, EmptyModelData.INSTANCE);
				
				ms.popPose();
			}
		}
	}
	
	@Override
	public boolean shouldRender(LayeredCannonBlockEntity be, Vec3 vec) {
		return be.getLayers().size() > 1 && super.shouldRender(be, vec);
	}

}
