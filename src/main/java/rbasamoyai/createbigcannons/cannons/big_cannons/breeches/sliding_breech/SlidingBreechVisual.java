package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech;

import com.simibubi.create.content.kinetics.base.ShaftVisual;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlock;

public class SlidingBreechVisual extends ShaftVisual<SlidingBreechBlockEntity> implements SimpleDynamicVisual {

	private final SlidingBreechBlockEntity breech;
	private OrientedInstance breechblock;
	private Direction blockRotation;

	public SlidingBreechVisual(VisualizationContext ctx, SlidingBreechBlockEntity tile, float partialTick) {
		super(ctx, tile, partialTick);
		this.breech = tile;
        Direction.Axis axis = CBCClientCommon.getRotationAxis(this.blockState);
        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
        this.blockRotation = facing.getCounterClockWise(axis);
        if (this.blockRotation == Direction.DOWN) this.blockRotation = Direction.UP;

        this.breechblock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCClientCommon.getBreechblockForState(this.blockState))).createInstance();

        boolean alongFirst = this.blockState.getValue(QuickfiringBreechBlock.AXIS);
        if (facing.getAxis().isHorizontal() && !alongFirst) {
            Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
            Quaternionf q = Axis.of(rotDir.step()).rotationDegrees(90f);
            this.breechblock.rotation(q);
        }
        if (facing.getAxis() == Direction.Axis.X && alongFirst) {
            this.breechblock.rotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
        }

        this.transformModels(partialTick);
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	private void transformModels(float partialTick) {
		float renderedBreechblockOffset = this.breech.getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
		renderedBreechblockOffset = renderedBreechblockOffset / 16.0f * 13.0f;
		Vector3f normal = this.blockRotation.step();
		normal.mul(renderedBreechblockOffset);
		this.breechblock.position(this.getVisualPosition()).translatePosition(normal.x(), normal.y(), normal.z());
	}

	@Override
	public void updateLight(float partialTick) {
		super.updateLight(partialTick);
		this.relight(this.pos, this.breechblock);
	}

	@Override
	public void _delete() {
		super._delete();
		this.breechblock.delete();
	}

}
