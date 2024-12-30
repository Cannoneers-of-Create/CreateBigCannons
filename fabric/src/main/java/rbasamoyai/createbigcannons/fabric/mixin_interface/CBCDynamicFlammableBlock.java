package rbasamoyai.createbigcannons.fabric.mixin_interface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface CBCDynamicFlammableBlock {

	int getFlammability(BlockState state, BlockGetter level, BlockPos pos);
	int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos);

}
