package rbasamoyai.createbigcannons.cannons.cannonend;

import java.util.Optional;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
	@Override public Direction.Axis getAxis(BlockState state) { return state.getValue(FACING).getAxis(); }
	@Override public Optional<Direction> getFacing(BlockState state) { return Optional.empty(); }
	@Override
	public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof SlidingBreechBlockEntity)) return CannonEnd.OPEN;
		return ((SlidingBreechBlockEntity) be).getOpenProgress() > 0.0f ? CannonEnd.OPEN : CannonEnd.CLOSED;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override public Class<SlidingBreechBlockEntity> getTileEntityClass() { return SlidingBreechBlockEntity.class; }
	@Override public BlockEntityType<? extends SlidingBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.SLIDING_BREECH.get(); }
	
}
