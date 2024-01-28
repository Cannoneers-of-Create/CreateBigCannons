package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface PropertiesMimicTypeBlock {
	public Block createBigCannons$getActualBlock(BlockState state, Level level, BlockPos pos);
}
