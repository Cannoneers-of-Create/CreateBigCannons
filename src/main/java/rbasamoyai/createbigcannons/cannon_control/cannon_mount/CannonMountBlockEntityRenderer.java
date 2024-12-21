package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.getAngleForTe;
import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.kineticRotationTransform;
import static com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer.shaft;

import org.joml.Quaternionf;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountBlockEntityRenderer extends SafeBlockEntityRenderer<CannonMountBlockEntity> {

	public CannonMountBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	protected void renderSafe(CannonMountBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (Backend.canUseInstancing(be.getLevel())) return;

		BlockState state = be.getBlockState();
		Direction vertical = state.getValue(BlockStateProperties.VERTICAL_DIRECTION);
		boolean upsideDown = vertical == Direction.UP;

		VertexConsumer solidBuf = buffer.getBuffer(RenderType.solid());

		ms.pushPose();

		SuperByteBuffer yawShaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, vertical);
		KineticBlockEntity yawInterface = be.getYawInterface();
		kineticRotationTransform(yawShaft, yawInterface, Direction.Axis.Y, getAngleForTe(yawInterface, be.getBlockPos(), Direction.Axis.Y), light)
			.renderInto(ms, solidBuf);

		Direction.Axis pitchAxis = ((IRotate) state.getBlock()).getRotationAxis(state);
		SuperByteBuffer pitchShaft = CachedBufferer.block(shaft(pitchAxis));
		KineticBlockEntity pitchInterface = be.getPitchInterface();
		kineticRotationTransform(pitchShaft, pitchInterface, pitchAxis, getAngleForTe(pitchInterface, be.getBlockPos(), pitchAxis), light)
			.renderInto(ms, solidBuf);

		float yaw = getMountYaw(be);
		Quaternionf qyaw = upsideDown ? Axis.ZP.rotationDegrees(180).mul(Axis.YP.rotationDegrees(yaw)) : Axis.YP.rotationDegrees(-yaw);
		CachedBufferer.partial(CBCBlockPartials.ROTATING_MOUNT, state)
			.translate(0.0d, upsideDown ? -1.0d : 1.0d, 0.0d)
			.light(light)
			.rotateCentered(qyaw)
			.renderInto(ms, solidBuf);

		float pitch = be.getPitchOffset(partialTicks);
		Quaternionf qpitch = upsideDown ? Axis.XP.rotationDegrees(pitch) : Axis.XP.rotationDegrees(-pitch);
		Quaternionf qyaw1 = new Quaternionf(qyaw);
		qyaw1.mul(qpitch);

		CachedBufferer.partialFacing(CBCBlockPartials.CANNON_CARRIAGE_AXLE, state, Direction.NORTH)
			.translate(0, upsideDown ? -2.0d : 2.0d, 0)
			.rotateCentered(qyaw1)
			.light(light)
			.renderInto(ms, solidBuf);

		ms.popPose();
	}

	private static float getMountYaw(CannonMountBlockEntity cmbe) {
		float time = AnimationTickHolder.getPartialTicks(cmbe.getLevel());
		return cmbe.getYawOffset(time);
	}

}
