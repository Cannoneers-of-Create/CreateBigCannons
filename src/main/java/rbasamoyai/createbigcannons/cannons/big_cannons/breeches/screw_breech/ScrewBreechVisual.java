package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;

import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class ScrewBreechVisual extends OrientedRotatingVisual<ScrewBreechBlockEntity> implements SimpleDynamicVisual {

	private final ScrewBreechBlockEntity breech;
	private final OrientedInstance screwLock;
	private final Direction facing;

	public ScrewBreechVisual(VisualizationContext ctx, ScrewBreechBlockEntity tile, float partialTick) {
		super(ctx, tile, partialTick, Direction.SOUTH, tile.getBlockState().getValue(BlockStateProperties.FACING), Models.partial(AllPartialModels.SHAFT_HALF));
		this.breech = tile;
        this.facing = tile.getBlockState().getValue(BlockStateProperties.FACING);
        this.screwLock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCClientCommon.getScrewBreechForState(this.blockState),
            this.facing)).createInstance();
        this.transformModels(partialTick);
	}

	private void transformModels(float partialTick) {
		float renderedScrewLockOffset = this.breech.getRenderedBlockOffset(partialTick);
		float heightOffset = renderedScrewLockOffset * 0.25f;
		float rotationOffset = renderedScrewLockOffset * 360.0f;
		Vector3f normal = this.facing.step();
		Vector3f height = new Vector3f(normal);
		height.mul(heightOffset);

		Quaternionf q = Axis.of(normal).rotationDegrees(rotationOffset);

		this.screwLock.position(this.getVisualPosition()).translatePosition(height.x(), height.y(), height.z()).rotation(q).setChanged();
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	@Override
	public void _delete() {
		super._delete();
		this.screwLock.delete();
	}

	@Override
	public void updateLight(float partialTick) {
		super.updateLight(partialTick);
		this.relight(this.pos, this.screwLock);
	}
}
