package rbasamoyai.createbigcannons.munitions.config;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface PropertiesMimicTypeBlock {

	@Nonnull Block createbigcannons$getActualBlock(BlockState state, Level level, BlockPos pos);

}
