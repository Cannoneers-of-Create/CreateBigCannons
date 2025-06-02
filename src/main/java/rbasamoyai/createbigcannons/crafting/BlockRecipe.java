package rbasamoyai.createbigcannons.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface BlockRecipe {

	boolean matches(Level level, BlockPos pos);

	void assembleInWorld(Level level, BlockPos pos);

	Block getResultBlock();

	BlockRecipeSerializer<?> getSerializer();

	BlockRecipeType<?> getType();

}
