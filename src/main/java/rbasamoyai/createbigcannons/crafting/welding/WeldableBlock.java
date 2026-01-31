package rbasamoyai.createbigcannons.crafting.welding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface WeldableBlock {

	boolean isWeldable(BlockState state);
	int weldDamage();
	boolean canWeldSide(Level level, Direction dir, BlockState state, BlockState otherState, BlockPos pos);
	void weldBlock(Level level, BlockState state, BlockPos pos, Direction dir);

}
