package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class FuzedBlockEntityRenderer extends SafeBlockEntityRenderer<FuzedBlockEntity> {

	public FuzedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super();
	}

	@Override
	protected void renderSafe(FuzedBlockEntity blockEntity, float partialTicks, PoseStack posestack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
		if (Backend.canUseInstancing(blockEntity.getLevel())) return;

		Direction facing = blockEntity.getBlockState().getValue(BlockStateProperties.FACING);
		if (blockEntity.hasFuze()) {
			SuperByteBuffer fuzeRender = CachedBufferer.partialFacing(CBCBlockPartials.FUZE, blockEntity.getBlockState(), facing);
			fuzeRender.renderInto(posestack, buffers.getBuffer(RenderType.cutout()));
		}
	}

}
