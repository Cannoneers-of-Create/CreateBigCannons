package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Constants;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountBlockEntityRenderer extends KineticTileEntityRenderer {

	public CannonMountBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(te.getLevel())) return;
		
		BlockState state = te.getBlockState();
		CannonMountBlockEntity cmbe = (CannonMountBlockEntity) te;
		
		VertexConsumer solidBuf = buffer.getBuffer(RenderType.solid());
		
		ms.pushPose();
		
		CachedBufferer.partialFacing(CBCBlockPartials.YAW_SHAFT, state, Direction.DOWN)
				.light(light)
				.rotateCentered(Direction.UP, getYawAngle(cmbe))
				.renderInto(ms, solidBuf);
		
		float yaw = getMountYaw(cmbe);
		Quaternion qyaw = Vector3f.YN.rotation(yaw);
		CachedBufferer.partial(CBCBlockPartials.ROTATING_MOUNT, state)
				.translate(0.0d, 1.0d, 0.0d)
				.light(light)
				.rotateCentered(qyaw)
				.renderInto(ms, solidBuf);
		
		float pitch = cmbe.getPitchOffset(partialTicks);
		Direction facing = cmbe.getContraptionDirection();
		boolean flag = (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X);
		Quaternion qpitch = Vector3f.XP.rotationDegrees(flag ? -pitch : pitch);
		Quaternion qyaw1 = qyaw.copy();
		qyaw1.mul(qpitch);
		
		CachedBufferer.partialFacing(CBCBlockPartials.CANNON_CARRIAGE_AXLE, state, Direction.NORTH)
				.translate(0, 2.0d, 0)
				.rotateCentered(qyaw1)
				.light(light)
				.renderInto(ms, solidBuf);
		
		ms.popPose();
	}
	
	private static float getYawAngle(CannonMountBlockEntity cmbe) {
		float time = AnimationTickHolder.getRenderTime(cmbe.getLevel());
		float offset = getRotationOffsetForPosition(cmbe, cmbe.getBlockPos(), Direction.Axis.Y);
		float angle = ((time * cmbe.getYawSpeed() * 3.0f / 10 + offset) % 360) / 180 * (float) Math.PI;
		return angle + getRotationOffsetForPosition(cmbe, cmbe.getBlockPos(), Direction.Axis.Y);
	}
	
	private static float getMountYaw(CannonMountBlockEntity cmbe) {
		float time = AnimationTickHolder.getPartialTicks(cmbe.getLevel());
		return cmbe.getYawOffset(time) * Constants.DEG_TO_RAD;
	}
	
	@Override
	protected BlockState getRenderedBlockState(KineticTileEntity te) {
		return shaft(getRotationAxisOf(te));
	}

}
