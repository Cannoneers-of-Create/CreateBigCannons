package rbasamoyai.createbigcannons.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface TransformableByBoring {

	BlockState getBoredBlockState(BlockState state, Level level, BlockPos pos);
	
}
