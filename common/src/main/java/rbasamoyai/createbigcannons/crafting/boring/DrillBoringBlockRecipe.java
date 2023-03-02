package rbasamoyai.createbigcannons.crafting.boring;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistryEntry;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

import java.util.List;

public class DrillBoringBlockRecipe implements BlockRecipe {

	private final BlockRecipeIngredient input;
	private final Block result;
	private final ResourceLocation id;

	public DrillBoringBlockRecipe(ResourceLocation id, BlockRecipeIngredient input, Block result) {
		this.id = id;
		this.input = input;
		this.result = result;
	}

	public boolean matches(BlockState state) { return this.input.test(state); }

	// Not used because this recipe type changes contraption blocks
	@Override public boolean matches(Level level, BlockPos pos) { return false; }
	@Override public void assembleInWorld(Level level, BlockPos pos) {}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.DRILL_BORING.get(); }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.DRILL_BORING.get(); }

	public List<ItemStack> ingredients() { return this.input.getBlockItems(); }

	public BlockState getResultState(BlockState input) {
		BlockState state = this.getResultBlock().defaultBlockState();
		for (Property<?> property : input.getProperties()) {
			if (state.hasProperty(property)) state = copyProperty(input, state, property);
		}
		return state;
	}

	private static <T extends Comparable<T>> BlockState copyProperty(BlockState src, BlockState dest, Property<T> property) {
		return dest.setValue(property, src.getValue(property));
	}

	public static class Serializer extends ForgeRegistryEntry<BlockRecipeSerializer<?>> implements BlockRecipeSerializer<DrillBoringBlockRecipe> {
		@Override
		public DrillBoringBlockRecipe fromJson(ResourceLocation id, JsonObject obj) {
			BlockRecipeIngredient input = BlockRecipeIngredient.fromJson(obj.get("input"));
			Block result = Registry.BLOCK.get(new ResourceLocation(obj.get("result").getAsString()));
			return new DrillBoringBlockRecipe(id, input, result);
		}

		@Override
		public DrillBoringBlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			BlockRecipeIngredient input = BlockRecipeIngredient.fromNetwork(buf);
			Block result = Registry.BLOCK.get(buf.readResourceLocation());
			return new DrillBoringBlockRecipe(id, input, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, DrillBoringBlockRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeResourceLocation(Registry.BLOCK.getKey(recipe.result));
		}
	}

}
