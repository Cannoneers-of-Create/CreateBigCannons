package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.ITransformableBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class UnboredSlidingBreechBlock extends UnboredCannonBlock implements ITransformableBlock {

	public static final BooleanProperty ALONG_FIRST = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
	
	public UnboredSlidingBreechBlock(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup, VoxelShape baseShape) {
		super(properties, material, CannonCastShape.SLIDING_BREECH, boredBlockSup, baseShape);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ALONG_FIRST);
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public BlockState getBoredBlockState(BlockState state) {
		BlockState bored = super.getBoredBlockState(state);
		return bored.hasProperty(ALONG_FIRST) ? bored.setValue(ALONG_FIRST, state.getValue(ALONG_FIRST)) : bored;
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
		if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1) state = state.cycle(ALONG_FIRST);
		return state.setValue(FACING, newFacing);
	}

}
