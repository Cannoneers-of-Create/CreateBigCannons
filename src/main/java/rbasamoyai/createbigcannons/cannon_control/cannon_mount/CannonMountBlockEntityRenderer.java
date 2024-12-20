package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.getAngleForTe;
import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.getRotationOffsetForPosition;
import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.kineticRotationTransform;
import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.shaft;

import org.joml.Quaternionf;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.Constants;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountBlockEntityRenderer extends SafeBlockEntityRenderer<CannonMountBlockEntity> {

	public CannonMountBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(CannonMountBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (Backend.canUseInstancing(be.getLevel())) return;

		BlockState state = be.getBlockState();

		VertexConsumer solidBuf = buffer.getBuffer(RenderType.solid());

		ms.pushPose();

		SuperByteBuffer yawShaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.DOWN);
		KineticBlockEntity yawInterface = be.getYawInterface();
		kineticRotationTransform(yawShaft, yawInterface, Direction.Axis.Y, getAngleForTe(yawInterface, be.getBlockPos(), Direction.Axis.Y), light)
			.renderInto(ms, solidBuf);

		Direction.Axis pitchAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
		SuperByteBuffer pitchShaft = CachedBufferer.block(shaft(pitchAxis));
		KineticBlockEntity pitchInterface = be.getPitchInterface();
		kineticRotationTransform(pitchShaft, pitchInterface, pitchAxis, getAngleForTe(pitchInterface, be.getBlockPos(), pitchAxis), light)
			.renderInto(ms, solidBuf);

		float yaw = getMountYaw(be);
		Quaternionf qyaw = Axis.YN.rotation(yaw);
		CachedBufferer.partial(CBCBlockPartials.ROTATING_MOUNT, state)
			.translate(0.0d, 1.0d, 0.0d)
			.light(light)
			.rotateCentered(qyaw)
			.renderInto(ms, solidBuf);

		float pitch = be.getPitchOffset(partialTicks);
		Quaternionf qpitch = Axis.XP.rotationDegrees(-pitch);
		Quaternionf qyaw1 = new Quaternionf(qyaw);
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
		float offset = getRotationOffsetForPosition(cmbe.getYawInterface(), cmbe.getBlockPos(), Direction.Axis.Y);
		float angle = ((time * cmbe.getYawSpeed() * 3.0f / 10 + offset) % 360) / 180 * (float) Math.PI;
		return angle + getRotationOffsetForPosition(cmbe.getYawInterface(), cmbe.getBlockPos(), Direction.Axis.Y);
	}

	private static float getMountYaw(CannonMountBlockEntity cmbe) {
		float time = AnimationTickHolder.getPartialTicks(cmbe.getLevel());
		return cmbe.getYawOffset(time) * Constants.DEG_TO_RAD;
	}

//	@Override
//	protected BlockState getRenderedBlockState(CannonMountBlockEntity be) {
//		return shaft(getRotationAxisOf(be));
//	}

}
