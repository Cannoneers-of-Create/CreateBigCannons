package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import java.util.function.Consumer;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class QuickfiringBreechVisual extends AbstractBlockEntityVisual<QuickfiringBreechBlockEntity> implements SimpleDynamicVisual {

	private final OrientedInstance breechblock;
	private final OrientedInstance shaft;
	private final OrientedInstance lever;
	private final Direction direction;
	private final Direction blockRotation;

	public QuickfiringBreechVisual(VisualizationContext ctx, QuickfiringBreechBlockEntity blockEntity, float partialTick) {
		super(ctx, blockEntity, partialTick);
        Direction.Axis axis = getRotationAxis(this.blockState);
        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
        Direction blockRotation = facing.getCounterClockWise(axis);
        this.direction = facing.getCounterClockWise(blockRotation.getAxis());

        if (blockRotation == Direction.DOWN)
            blockRotation = Direction.UP;
        this.blockRotation = blockRotation;

        this.breechblock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(this.blockState)))
            .createInstance();
        this.shaft = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(AllPartialModels.SHAFT))
            .createInstance()
            .rotateTo(Direction.UP, this.direction)
            .position(this.getVisualPosition());
        this.lever = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.QUICKFIRING_BREECH_LEVER, this.direction))
            .createInstance();

        boolean alongFirst = this.blockState.getValue(QuickfiringBreechBlock.AXIS);
        if (facing.getAxis().isHorizontal()) {
            if (this.blockRotation.getAxis().isHorizontal()) {
                this.breechblock.rotateTo(Direction.NORTH, this.blockRotation).rotateDegrees(90f, Direction.NORTH);
            } else {
                this.breechblock.rotateTo(Direction.NORTH, Direction.UP);
                if (facing.getAxis() == Direction.Axis.X)
                    this.breechblock.rotateZDegrees(90f);
            }
        } else if (!alongFirst) {
            this.breechblock.rotateYDegrees(90f);
        }
        this.transformModels(partialTick);
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	private void transformModels(float partialTick) {
		float progress = this.blockEntity.getOpenProgress(partialTick);
		BlockPos visualPos = this.getVisualPosition();

		float renderedBreechblockOffset = progress / 16.0f * 13.0f;
		Vector3f normal = this.blockRotation.step();
		normal.mul(renderedBreechblockOffset);
		this.breechblock.position(visualPos).translatePosition(normal.x(), normal.y(), normal.z()).setChanged();

		float angle = progress * 90;
		Quaternionf qrot = Axis.of(this.direction.step()).rotationDegrees(angle);
		this.shaft.position(visualPos)
            .identityRotation()
            .rotate(qrot)
            .rotateTo(Direction.UP, this.direction)
            .setChanged();
		this.lever.position(visualPos.relative(this.direction)).rotation(qrot).setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.pos, this.breechblock);
		this.relight(this.pos, this.shaft);
		this.relight(this.pos, this.lever);
	}

	@Override
	public void _delete() {
		this.breechblock.delete();
		this.shaft.delete();
		this.lever.delete();
	}

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(breechblock);
        consumer.accept(shaft);
        consumer.accept(lever);
    }

	private static PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof BigCannonBlock cBlock ? CBCBlockPartials.breechblockFor(cBlock.getCannonMaterial())
			: CBCBlockPartials.CAST_IRON_SLIDING_BREECHBLOCK;
	}

	private static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(QuickfiringBreechBlock.AXIS);
		return switch (state.getValue(QuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

}
