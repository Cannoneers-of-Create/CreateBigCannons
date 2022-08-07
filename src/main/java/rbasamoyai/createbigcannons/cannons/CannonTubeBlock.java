package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;

public class CannonTubeBlock extends WrenchableDirectionalBlock implements ITE<CannonBlockEntity>, CannonBlock {
	
	public static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;
	
	private final CannonMaterial material;
	
	public CannonTubeBlock(Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
	}
	
	@Override public CannonMaterial getCannonMaterial() { return this.material; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.OPEN; }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }	

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) CannonBlock.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override public Class<CannonBlockEntity> getTileEntityClass() { return CannonBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON.get(); }
	
}
