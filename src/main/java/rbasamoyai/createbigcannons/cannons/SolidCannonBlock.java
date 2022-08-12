package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlockEntity;

public class SolidCannonBlock extends WrenchableDirectionalBlock implements ITE<CannonEndBlockEntity>, CannonBlock {

	private final CannonMaterial cannonMaterial;
	
	public SolidCannonBlock(Properties properties, CannonMaterial material) {
		super(properties);
		this.cannonMaterial = material;
	}
	
	@Override public CannonMaterial getCannonMaterial() { return this.cannonMaterial; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING).getOpposite(); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) CannonBlock.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override public Class<CannonEndBlockEntity> getTileEntityClass() { return CannonEndBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonEndBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_END.get(); }

}
