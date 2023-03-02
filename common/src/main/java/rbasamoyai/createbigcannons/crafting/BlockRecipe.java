package rbasamoyai.createbigcannons.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface BlockRecipe {

	boolean matches(Level level, BlockPos pos);
	
	void assembleInWorld(Level level, BlockPos pos);
	
	Block getResultBlock();
	
	ResourceLocation getId();
	
	BlockRecipeSerializer<?> getSerializer();
	
	BlockRecipeType<?> getType();
	
}
