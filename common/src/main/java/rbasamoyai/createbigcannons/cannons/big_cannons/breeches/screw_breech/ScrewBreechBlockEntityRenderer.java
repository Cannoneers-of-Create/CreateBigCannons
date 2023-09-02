package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class ScrewBreechBlockEntityRenderer extends KineticBlockEntityRenderer {

	public ScrewBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(te.getLevel())) return;

		BlockState blockState = te.getBlockState();
		Direction facing = blockState.getValue(BlockStateProperties.FACING);

		float renderedScrewLockOffset = ((ScrewBreechBlockEntity) te).getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
		float heightOffset = renderedScrewLockOffset * 0.25f;
		float rotationOffset = renderedScrewLockOffset * (facing.getAxisDirection() == AxisDirection.POSITIVE ? 360.0f : -360.0f);
		Vector3f normal = facing.step();
		Vector3f height = new Vector3f(normal);
		height.mul(heightOffset);

		Quaternionf q = Axis.of(normal).rotationDegrees(rotationOffset);

		ms.pushPose();

		SuperByteBuffer screwLockRender = CachedBufferer.partialFacing(CBCClientCommon.getScrewBreechForState(blockState), blockState, facing);
		screwLockRender
			.translate(height.x(), height.y(), height.z())
			.rotateCentered(q)
			.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));

		ms.popPose();
	}

	@Override
	protected SuperByteBuffer getRotatedModel(KineticBlockEntity te, BlockState state) {
		return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, state.getValue(BlockStateProperties.FACING));
	}

}
