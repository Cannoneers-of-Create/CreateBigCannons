package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

public interface PropertiesMimicTypeBlock {
	@NotNull Block createBigCannons$getActualBlock(BlockState state, Level level, BlockPos pos);
}
