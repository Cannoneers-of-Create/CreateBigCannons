package rbasamoyai.createbigcannons.compat.jei;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

public abstract class HardcodedBlockRecipe implements BlockRecipe {

	private final ResourceLocation id;
	private final Block result;
	
	public HardcodedBlockRecipe(ResourceLocation id, Block result) {
		this.id = id;
		this.result = result;
	}
	
	@Override public boolean matches(Level level, BlockPos pos) { return false; }
	@Override public void assembleInWorld(Level level, BlockPos pos) {}
	
	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	
	@Override public BlockRecipeSerializer<?> getSerializer() { return null; }
	@Override public BlockRecipeType<?> getType() { return null; }
	
	public abstract List<ItemStack> ingredients();
	
}
