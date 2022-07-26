package rbasamoyai.createbigcannons.cannons.cannonend;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.cannons.CannonBlock;

public class SlidingBreechBlockEntityRenderer extends KineticTileEntityRenderer {

	public SlidingBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	@Override
	public boolean shouldRenderOffScreen(KineticTileEntity te) {
		return true;
	}
	
	@Override
	protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
		
		BlockState blockState = te.getBlockState();
		
		if (Backend.canUseInstancing(te.getLevel())) return;
		
		Direction facing = blockState.getValue(FACING);
		Direction.Axis axis;
		boolean horizontal = facing.getAxis().isHorizontal();
		boolean alongFirst = blockState.getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
		boolean isX = facing.getAxis() == Direction.Axis.X;
		
		ms.pushPose();
		
		Quaternion qrot;
		
		if (horizontal && (facing.getAxis() == Direction.Axis.X) != alongFirst) {
			Direction dir = isX ? Direction.SOUTH : Direction.EAST;
			Quaternion q = Direction.UP.step().rotationDegrees(AngleHelper.horizontalAngle(facing) + (isX ? 90.0f : 0.0f));
			Quaternion q1 = dir.step().rotationDegrees(90.0f);
			q.mul(q1);
			qrot = q;
			axis = Direction.Axis.Y;
		} else if (horizontal) {
			Direction dir = isX ? Direction.EAST : Direction.SOUTH;
			Quaternion q = Direction.UP.step().rotationDegrees(AngleHelper.horizontalAngle(facing) + (isX ? 0.0f : 90.0f));
			Quaternion q1 = dir.step().rotationDegrees(90.0f);
			q.mul(q1);
			qrot = q;
			axis = alongFirst ? Direction.Axis.Z : Axis.X;
		} else {
			Quaternion q = Direction.UP.step().rotationDegrees(alongFirst ? 0.0f : 90.0f);
			Quaternion q1 = Direction.EAST.step().rotationDegrees(90.0f);
			q.mul(q1);
			qrot = q;
			axis = alongFirst ? Direction.Axis.Z : Direction.Axis.X;
		}
		
		float renderedBreechblockOffset = ((SlidingBreechBlockEntity) te).getRenderedBlockOffset(partialTicks);
		renderedBreechblockOffset = renderedBreechblockOffset / 16.0f * 13.0f;
		Vector3f normal = Direction.fromAxisAndDirection(axis, axis == Axis.Y ? Direction.AxisDirection.POSITIVE : facing.getAxisDirection()).step();
		normal.mul(renderedBreechblockOffset);
		
		SuperByteBuffer breechblockRender = CachedBufferer.partialFacing(this.getPartialModelForState(blockState), blockState, facing);
		breechblockRender
				.translate(normal.x(), normal.y(), normal.z())
				.rotateCentered(qrot)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		
		ms.popPose();
	}
	
	@Override
	protected BlockState getRenderedBlockState(KineticTileEntity te) {
		return shaft(getRotationAxisOf(te));
	}
	
	private PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof CannonBlock ? CBCBlockPartials.breechblockFor(((CannonBlock) state.getBlock()).getCannonMaterial()) : CBCBlockPartials.CAST_IRON_SLIDING_BREECHBLOCK;
	}
	
}
