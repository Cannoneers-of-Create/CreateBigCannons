package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class UnboredSlidingBreechBlock extends UnboredBigCannonBlock implements ITransformableBlock {

	public static final BooleanProperty ALONG_FIRST = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

	public UnboredSlidingBreechBlock(Properties properties, BigCannonMaterial material, VoxelShape baseShape) {
		super(properties, material, () -> CannonCastShape.SLIDING_BREECH, baseShape);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ALONG_FIRST);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		Direction horizontal = context.getHorizontalDirection();
		return this.defaultBlockState()
			.setValue(FACING, facing)
			.setValue(ALONG_FIRST, horizontal.getAxis() == Direction.Axis.Z);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		if (rotation.ordinal() % 2 == 1) state = state.cycle(ALONG_FIRST);
		return super.rotate(state, rotation);
	}

	@Override
	public BlockState transform(BlockState state, StructureTransform transform) {
		if (transform.mirror != null) state = this.mirror(state, transform.mirror);
		if (transform.rotationAxis == Direction.Axis.Y) return this.rotate(state, transform.rotation);
		Direction newFacing = transform.rotateFacing(state.getValue(FACING));
		if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1)
			state = state.cycle(ALONG_FIRST);
		return state.setValue(FACING, newFacing);
	}

}
