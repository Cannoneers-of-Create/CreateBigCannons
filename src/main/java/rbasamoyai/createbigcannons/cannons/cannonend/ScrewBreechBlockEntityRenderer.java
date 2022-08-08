package rbasamoyai.createbigcannons.cannons.cannonend;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
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
import rbasamoyai.createbigcannons.CBCBlockPartials;
import rbasamoyai.createbigcannons.cannons.CannonBlock;

public class ScrewBreechBlockEntityRenderer extends KineticTileEntityRenderer {

	public ScrewBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(te.getLevel())) return;
		
		BlockState blockState = te.getBlockState();
		Direction facing = blockState.getValue(BlockStateProperties.FACING);
		
		float renderedScrewLockOffset = ((ScrewBreechBlockEntity) te).getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
		float heightOffset = renderedScrewLockOffset * 0.25f;
		float rotationOffset = renderedScrewLockOffset * (facing.getAxisDirection() == AxisDirection.POSITIVE ? 360.0f : -360.0f);
		Vector3f normal = facing.step();
		Vector3f height = normal.copy();
		height.mul(heightOffset);
		
		boolean isY = facing.getAxis() == Direction.Axis.Y;
		Quaternion q = Vector3f.XP.rotationDegrees(90.0f);
		Quaternion q1 = Vector3f.YP.rotationDegrees(isY ? 0.0f : -facing.toYRot());
		Quaternion q2 = normal.rotationDegrees(rotationOffset);
		q1.mul(q);
		q2.mul(q1);
		
		ms.pushPose();
		
		SuperByteBuffer screwLockRender = CachedBufferer.partialFacing(this.getPartialModelForState(blockState), blockState, facing);
		screwLockRender
				.translate(height.x(), height.y(), height.z())
				.rotateCentered(q2)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		
		ms.popPose();
	}
	
	@Override
	protected SuperByteBuffer getRotatedModel(KineticTileEntity te, BlockState state) {
		return CachedBufferer.partialFacing(AllBlockPartials.SHAFT_HALF, state, state.getValue(BlockStateProperties.FACING));
	}
	
	private PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof CannonBlock ? CBCBlockPartials.screwLockFor(((CannonBlock) state.getBlock()).getCannonMaterial()) : CBCBlockPartials.STEEL_SCREW_LOCK;
	}

}
