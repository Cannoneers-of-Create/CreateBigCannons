package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class SlidingBreechBlock extends DirectionalAxisKineticBlock implements ITE<SlidingBreechBlockEntity>, CannonBlock {

	private final CannonMaterial cannonMaterial;
	
	public SlidingBreechBlock(Properties properties, CannonMaterial cannonMaterial) {
		super(properties);
		this.cannonMaterial = cannonMaterial;
	}

	@Override public CannonMaterial getCannonMaterial() { return this.cannonMaterial; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	
	@Override
	public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof SlidingBreechBlockEntity breech)) return CannonEnd.OPEN;
		return breech.getOpenProgress() > 0.0f ? CannonEnd.OPEN : CannonEnd.CLOSED;
	}
	
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		return this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(AXIS_ALONG_FIRST_COORDINATE, facing.getAxis() == Direction.Axis.Z);
	}
	
	@Override public InteractionResult onWrenched(BlockState state, UseOnContext context) { return InteractionResult.PASS; }
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) CannonBlock.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override public Class<SlidingBreechBlockEntity> getTileEntityClass() { return SlidingBreechBlockEntity.class; }
	@Override public BlockEntityType<? extends SlidingBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.SLIDING_BREECH.get(); }
	
	@Override public boolean canInteractWithDrill(BlockState state) { return false; }
	@Override public boolean isComplete(BlockState state) { return true; }
	
}
