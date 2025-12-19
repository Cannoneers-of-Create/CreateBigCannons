package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class FuzedBlockEntityRenderer extends SafeBlockEntityRenderer<FuzedBlockEntity> {

	public FuzedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();
	}

	@Override
	protected void renderSafe(FuzedBlockEntity blockEntity, float partialTicks, PoseStack posestack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		if (VisualizationManager.supportsVisualization(blockEntity.getLevel())) return;

		BlockState state = blockEntity.getBlockState();
		Direction facing = state.getValue(BlockStateProperties.FACING);
		if (state.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzed && fuzed.isBaseFuze())
			facing = facing.getOpposite();
		if (blockEntity.hasFuze()) {
			SuperByteBuffer fuzeRender = CachedBuffers.partialFacing(CBCBlockPartials.FUZE, blockEntity.getBlockState(), facing)
                .light(packedLight);
			fuzeRender.renderInto(posestack, buffers.getBuffer(RenderType.cutout()));
		}
	}

}
