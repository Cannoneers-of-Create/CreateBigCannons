package rbasamoyai.createbigcannons.remix;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface CBCExplodableBlock {

	default void createbigcannons$onBlockExplode(Level level, BlockPos pos, BlockState state, Explosion explosion) {}

}
