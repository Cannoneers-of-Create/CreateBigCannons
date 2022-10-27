package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class SlidingBreechBlock extends DirectionalAxisKineticBlock implements ITE<SlidingBreechBlockEntity>, CannonBlock {

	private final CannonMaterial cannonMaterial;
	
	public SlidingBreechBlock(Properties properties, CannonMaterial cannonMaterial) {
		super(properties);
		this.cannonMaterial = cannonMaterial;
	}

	@Override public CannonMaterial getCannonMaterial() { return this.cannonMaterial; }
	@Override public CannonCastShape getCannonShape() { return CannonCastShape.SLIDING_BREECH.get(); }	
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	
	@Override
	public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof SlidingBreechBlockEntity breech ? breech.getOpeningType() : CannonEnd.OPEN;
	}
	
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		Direction horizontal = context.getHorizontalDirection();
		return this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(AXIS_ALONG_FIRST_COORDINATE, horizontal.getAxis() == Direction.Axis.Z);
	}
	
	@Override public InteractionResult onWrenched(BlockState state, UseOnContext context) { return InteractionResult.PASS; }
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override public Class<SlidingBreechBlockEntity> getTileEntityClass() { return SlidingBreechBlockEntity.class; }
	@Override public BlockEntityType<? extends SlidingBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.SLIDING_BREECH.get(); }
	
	@Override public boolean isComplete(BlockState state) { return true; }
	
}
