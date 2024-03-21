package rbasamoyai.createbigcannons.block_hardness;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface TerminalBallisticsBlockPropertiesProvider {

	double hardness(Level level, BlockState state, BlockPos pos, boolean recurse);

}
