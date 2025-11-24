package rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.joml.Vector3f;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class AutocannonRecoilSpringVisual extends AbstractBlockEntityVisual<AutocannonRecoilSpringBlockEntity> implements SimpleDynamicVisual {

	private final TransformedInstance spring;
	private final Map<BlockPos, OrientedInstance> blocks = new HashMap<>();

	private final Direction facing;

	public AutocannonRecoilSpringVisual(VisualizationContext ctx, AutocannonRecoilSpringBlockEntity blockEntity, float partialTicks) {
		super(ctx, blockEntity, partialTicks);
        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        this.spring = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getPartialModelForState(), this.facing)).createInstance();

        this.blocks.clear();
        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks.put(entry.getKey(), instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(entry.getValue())).createInstance());
        }

        this.updateTransforms(partialTicks);
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.updateTransforms(ctx.partialTick());
	}

	private void updateTransforms(float partialTicks) {
		boolean flag = this.facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
		BlockPos pos = this.getVisualPosition().relative(this.facing.getOpposite(), flag ? 1 : 0);
		Vec3 pivot = Vec3.atLowerCornerOf(pos);
		float scale = this.blockEntity.getAnimateOffset(partialTicks);
		float f1 = scale * 0.5f + 0.5f;
		Direction.Axis axis = this.facing.getAxis();

		float fx = axis == Direction.Axis.X ? f1 : 1;
		float fy = axis == Direction.Axis.Y ? f1 : 1;
		float fz = axis == Direction.Axis.Z ? f1 : 1;

		this.spring.setIdentityTransform()
            .translate(pivot);
		if (flag) {
			this.spring.rotateCentered(Mth.PI, axis.isVertical() ? Direction.EAST : Direction.UP)
				.translate(this.facing.getOpposite().step());
		}
		this.spring.scale(fx, fy, fz);
        this.spring.setChanged();

		Vector3f offs = this.facing.step();
		offs.mul((1 - scale) * -0.5f);
		offs.add(this.getVisualPosition().getX(), this.getVisualPosition().getY(), this.getVisualPosition().getZ());

		for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks.entrySet()) {
			BlockPos pos1 = entry.getKey();
			entry.getValue().position(offs).translatePosition(pos1.getX(), pos1.getY(), pos1.getZ()).setChanged();
		}
	}

	@Override
	public void updateLight(float partialTicks) {
		this.relight(this.pos, this.spring);
		for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks.entrySet()) {
			this.relight(this.pos.offset(entry.getKey()), entry.getValue());
		}
	}

	@Override
	protected void _delete() {
		this.spring.delete();
		for (OrientedInstance block : this.blocks.values())
            block.delete();
	}

	private PartialModel getPartialModelForState() {
		return this.blockState.getBlock() instanceof AutocannonBlock cBlock
			? CBCBlockPartials.autocannonSpringFor(cBlock.getAutocannonMaterial())
			: CBCBlockPartials.CAST_IRON_AUTOCANNON_SPRING;
	}

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(this.spring);
    }

}
