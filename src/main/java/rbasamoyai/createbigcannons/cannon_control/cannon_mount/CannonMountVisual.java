package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import java.util.function.Consumer;

import org.joml.Quaternionf;

import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CannonMountVisual extends KineticBlockEntityVisual<CannonMountBlockEntity> implements SimpleDynamicVisual {

	private final OrientedInstance rotatingMount;
	private final OrientedInstance rotatingMountShaft;
	private final RotatingInstance pitchShaft;
	private final RotatingInstance yawShaft;

	public CannonMountVisual(VisualizationContext ctx, CannonMountBlockEntity tile, float partialTick) {
        super(ctx, tile, partialTick);

        Direction vertical = tile.getBlockState().getValue(BlockStateProperties.VERTICAL_DIRECTION);
        Direction facing = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

        this.rotatingMount = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.ROTATING_MOUNT))
            .createInstance()
            .position(this.getVisualPosition().relative(vertical.getOpposite()));

        this.rotatingMountShaft = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.CANNON_CARRIAGE_AXLE))
            .createInstance()
            .position(this.getVisualPosition().relative(vertical, -2));

        this.pitchShaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT))
            .createInstance()
            .rotateToFace(pitchAxis)
            .setup(this.blockEntity.getPitchInterface())
            .setPosition(this.getVisualPosition());

        this.yawShaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
            .createInstance()
            .rotateToFace(Direction.SOUTH, vertical)
            .setup(this.blockEntity.getYawInterface())
            .setPosition(this.getVisualPosition());

        this.transformModels();
	}

	@Override
	public void _delete() {
		this.rotatingMount.delete();
		this.rotatingMountShaft.delete();
		this.pitchShaft.delete();
		this.yawShaft.delete();
	}

	private void transformModels() {
		Direction facing = this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction.Axis pitchAxis = facing.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;

		this.updateRotation(this.pitchShaft, pitchAxis, this.blockEntity.getPitchSpeed(), true);
		this.updateRotation(this.yawShaft, Direction.Axis.Y, this.blockEntity.getYawSpeed(), false);
	}

    // Copied from KineticBlockEntityInstance
    protected void updateRotation(RotatingInstance instance, Direction.Axis axis, float speed, boolean pitch) {
        instance.setRotationAxis(axis)
            .setRotationOffset(rotationOffset(this.blockState, axis, this.pos))
            .setRotationalSpeed(speed * RotatingInstance.SPEED_MULTIPLIER);
        if (KineticDebugger.isActive())
            instance.setColor(pitch ? this.blockEntity.getPitchInterface() : this.blockEntity.getYawInterface());
        instance.setChanged();
    }


	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels();
		float partialTicks = ctx.partialTick();
		boolean upsideDown = this.blockState.getValue(BlockStateProperties.VERTICAL_DIRECTION) == Direction.UP;

		float yaw = this.blockEntity.getYawOffset(partialTicks - 1);
		Quaternionf qyaw = upsideDown ? Axis.ZP.rotationDegrees(180).mul(Axis.YP.rotationDegrees(yaw)) : Axis.YP.rotationDegrees(-yaw);
		this.rotatingMount.rotation(qyaw);
		float pitch = this.blockEntity.getPitchOffset(partialTicks - 1);
		Quaternionf qpitch = upsideDown ? Axis.XP.rotationDegrees(pitch) : Axis.XP.rotationDegrees(-pitch);
		Quaternionf qyaw1 = new Quaternionf(qyaw);
		qyaw1.mul(qpitch);
		this.rotatingMountShaft.rotation(qyaw1);
        this.rotatingMount.setChanged();
        this.rotatingMountShaft.setChanged();
        this.pitchShaft.setChanged();
        this.yawShaft.setChanged();
	}

    @Override
    public void updateLight(float partialTicks) {
		Direction vertical = this.blockState.getValue(BlockStateProperties.VERTICAL_DIRECTION).getOpposite();
		this.relight(this.pos.relative(vertical), this.rotatingMount);
		this.relight(this.pos.relative(vertical, 2), this.rotatingMountShaft);
		this.relight(this.pos, this.pitchShaft);
		this.relight(this.pos, this.yawShaft);
	}

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(this.rotatingMount);
        consumer.accept(this.rotatingMountShaft);
        consumer.accept(this.pitchShaft);
        consumer.accept(this.yawShaft);
    }

}
