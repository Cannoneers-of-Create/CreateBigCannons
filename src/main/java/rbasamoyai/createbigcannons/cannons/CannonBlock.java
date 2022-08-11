package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;

public interface CannonBlock {

	CannonMaterial getCannonMaterial();
	default CannonMaterial getCannonMaterial(Level level, BlockState state, BlockPos pos) { return this.getCannonMaterial(); }
	
	Direction getFacing(BlockState state);
	CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos);
	default boolean isDoubleSidedCannon(BlockState state) { return true; }
	default boolean isImmovable(BlockState state) { return false; }
	
	public static void onRemoveCannon(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() instanceof CannonBlock cBlock) {	
			Direction facing = cBlock.getFacing(state);
			CannonMaterial material = cBlock.getCannonMaterial();				
			
			BlockPos pos1 = pos.relative(facing);
			BlockState state1 = level.getBlockState(pos1);
			if (state1.getBlock() instanceof CannonBlock cBlock1
			&& cBlock1.getCannonMaterial() == material
			&& level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe1) {
				Direction facing1 = cBlock1.getFacing(state1);
				if (facing == facing1.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && facing.getAxis() == facing1.getAxis()) {
					cbe1.cannonBehavior().setConnectedFace(facing.getOpposite(), false);
				}
			}
			BlockPos pos2 = pos.relative(facing.getOpposite());
			BlockState state2 = level.getBlockState(pos2);
			if (cBlock.isDoubleSidedCannon(state)
			&& state2.getBlock() instanceof CannonBlock cBlock2
			&& cBlock2.getCannonMaterial() == material
			&& level.getBlockEntity(pos2) instanceof ICannonBlockEntity cbe2) {
				Direction facing2 = cBlock2.getFacing(state2);
				if (facing == facing2 || cBlock2.isDoubleSidedCannon(state2) && facing.getAxis() == facing2.getAxis()) {
					cbe2.cannonBehavior().setConnectedFace(facing, false);
				}
			}
		}
	}
	
}
