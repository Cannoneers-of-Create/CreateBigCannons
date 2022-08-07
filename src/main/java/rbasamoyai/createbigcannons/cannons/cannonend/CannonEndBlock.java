package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class CannonEndBlock extends WrenchableDirectionalBlock implements ITE<CannonEndBlockEntity>, CannonBlock {

	private final CannonMaterial cannonMaterial;
	private final VoxelShaper shapes;
	
	public CannonEndBlock(Properties properties, CannonMaterial cannonMaterial) {
		super(properties);
		this.cannonMaterial = cannonMaterial;
		this.shapes = this.makeShapes();
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 10, 10), box(5, 10, 5, 11, 16, 11));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override public CannonMaterial getCannonMaterial() { return this.cannonMaterial; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING).getOpposite(); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }
	@Override public boolean isDoubleSidedCannon(BlockState state) { return false; }
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) CannonBlock.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override public Class<CannonEndBlockEntity> getTileEntityClass() { return CannonEndBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonEndBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_END.get(); }
	
}
