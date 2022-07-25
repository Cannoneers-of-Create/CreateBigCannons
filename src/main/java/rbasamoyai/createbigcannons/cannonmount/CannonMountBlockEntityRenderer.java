package rbasamoyai.createbigcannons.cannonmount;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Constants;
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
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockPartials;

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
		
		SuperByteBuffer yawShaftRender = CachedBufferer.partialFacing(CBCBlockPartials.YAW_SHAFT, state, Direction.DOWN);
		yawShaftRender.light(light).rotateCentered(Direction.UP, getYawAngle(cmbe)).renderInto(ms, solidBuf);
		
		float yaw = getMountYaw(cmbe);
		Quaternion qyaw = Vector3f.YN.rotation(yaw);
		SuperByteBuffer rotatingMount = CachedBufferer.partial(CBCBlockPartials.ROTATING_MOUNT, state);
		rotatingMount.translate(0.0d, 1.0d, 0.0d).light(light).rotateCentered(qyaw).renderInto(ms, solidBuf);
		
		float pitch = cmbe.getPitchOffset(partialTicks);
		Quaternion qpitch = Vector3f.XP.rotationDegrees(cmbe.getContraptionDirection() == Direction.SOUTH ? -pitch : pitch);
		Quaternion qyaw1 = qyaw.copy();
		qyaw1.mul(qpitch);
		
		float nx = Mth.cos(yaw);
		float nz = Mth.sin(yaw);
		
		SuperByteBuffer rotatingMountShaft1 = CachedBufferer.partialFacing(AllBlockPartials.SHAFT_HALF, state, Direction.EAST);
		rotatingMountShaft1
				.translate(nx * 0.25d, 2.0d, nz * 0.25d)
				.rotateCentered(qyaw1)
				.light(light)
				.renderInto(ms, solidBuf);
		
		SuperByteBuffer rotatingMountShaft2 = CachedBufferer.partialFacing(AllBlockPartials.SHAFT_HALF, state, Direction.WEST);
		rotatingMountShaft2
				.translate(nx * -0.25d, 2.0d, nz * -0.25d)
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
