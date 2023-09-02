package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class QuickfiringBreechBlockEntityRenderer extends SafeBlockEntityRenderer<QuickfiringBreechBlockEntity> {

	public QuickfiringBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public boolean shouldRenderOffScreen(QuickfiringBreechBlockEntity blockEntity) {
		return true;
	}

	@Override
	protected void renderSafe(QuickfiringBreechBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockState = te.getBlockState();

		if (Backend.canUseInstancing(te.getLevel())) return;

		Direction facing = blockState.getValue(BlockStateProperties.FACING);
		Direction.Axis axis = CBCClientCommon.getRotationAxis(blockState);
		Direction blockRotation = facing.getCounterClockWise(axis);
		if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;

		Quaternionf qrot;

		boolean alongFirst = blockState.getValue(QuickfiringBreechBlock.AXIS);
		if (facing.getAxis().isHorizontal() && !alongFirst) {
			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
			qrot = Axis.of(rotDir.step()).rotationDegrees(90f);
		} else if (facing.getAxis() == Direction.Axis.X && alongFirst) {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
		} else {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(0);
		}

		VertexConsumer vcons = buffer.getBuffer(RenderType.solid());

		ms.pushPose();

		float progress = te.getOpenProgress(partialTicks);
		float renderedBreechblockOffset = progress / 16.0f * 13.0f;
		Vector3f normal = blockRotation.step();
		normal.mul(renderedBreechblockOffset);

		CachedBufferer.partialFacing(CBCClientCommon.getBreechblockForState(blockState), blockState, blockRotation)
			.translate(normal.x(), normal.y(), normal.z())
			.rotateCentered(qrot)
			.light(light)
			.renderInto(ms, vcons);

		ms.popPose();
		ms.pushPose();

		float angle = progress * 90;
		Direction dir = facing.getCounterClockWise(blockRotation.getAxis());
		Vector3f normal1 = dir.step();
		Axis axis1 = Axis.of(normal1);

		CachedBufferer.block(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, axis))
			.rotateCentered(axis1.rotationDegrees(angle))
			.light(light)
			.renderInto(ms, vcons);

		ms.popPose();
		ms.pushPose();

		CachedBufferer.partialFacing(CBCBlockPartials.QUICKFIRING_BREECH_LEVER, blockState, dir)
			.rotateCentered(axis1.rotationDegrees(angle))
			.translate(normal1)
			.light(light)
			.renderInto(ms, vcons);

		ms.popPose();
	}

}
