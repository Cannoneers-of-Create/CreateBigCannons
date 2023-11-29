package rbasamoyai.createbigcannons.cannons.big_cannons.drop_mortar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.SolidBigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCShapes;

public class DropMortarEndBlock extends SolidBigCannonBlock<BigCannonEndBlockEntity> {

	public DropMortarEndBlock(Properties properties, BigCannonMaterial cannonMaterial) {
		super(properties, cannonMaterial);
	}

	@Override public boolean canConnectToSide(BlockState state, Direction dir) { return this.getFacing(state) == dir; }

	@Override
	public Direction getFacing(BlockState state) {
		return super.getFacing(state).getOpposite();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CBCShapes.CANNON_END.get(state.getValue(FACING));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CBCShapes.DROP_MORTAR_END.get(state.getValue(FACING));
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.DROP_MORTAR_END;
	}

	@Override
	public Class<BigCannonEndBlockEntity> getBlockEntityClass() {
		return BigCannonEndBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BigCannonEndBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_END.get();
	}

}
