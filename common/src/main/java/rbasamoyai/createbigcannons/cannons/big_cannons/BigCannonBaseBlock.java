package rbasamoyai.createbigcannons.cannons.big_cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;

public abstract class BigCannonBaseBlock extends DirectionalBlock implements BigCannonBlock {

	private final BigCannonMaterial material;

	protected BigCannonBaseBlock(Properties properties, BigCannonMaterial material) {
		super(properties.pushReaction(PushReaction.BLOCK));
		this.material = material;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.createBlockStateDefinition(builder);
	}

	@Override public BigCannonMaterial getCannonMaterial() { return this.material; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	@Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
	@Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

}
