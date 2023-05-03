package rbasamoyai.createbigcannons.crafting.boring;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

import java.util.List;

public class DrillBoringBlockRecipe implements BlockRecipe {

	private final BlockRecipeIngredient input;
	private final Block result;
	private final ResourceLocation id;
	private final boolean obeyFacingOrAxis;

	public DrillBoringBlockRecipe(ResourceLocation id, BlockRecipeIngredient input, Block result, boolean obeyFacingOrAxis) {
		this.id = id;
		this.input = input;
		this.result = result;
		this.obeyFacingOrAxis = obeyFacingOrAxis;
	}

	public boolean matches(BlockState state, Direction dir) {
		if (this.obeyFacingOrAxis) {
			if (state.hasProperty(BlockStateProperties.FACING) && state.getValue(BlockStateProperties.FACING).getAxis() != dir.getAxis())
				return false;
			if (state.hasProperty(BlockStateProperties.AXIS) && state.getValue(BlockStateProperties.AXIS) != dir.getAxis())
				return false;
		}
		return this.input.test(state);
	}

	// Not used because this recipe type changes contraption blocks
	@Override public boolean matches(Level level, BlockPos pos) { return false; }
	@Override public void assembleInWorld(Level level, BlockPos pos) {}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.DRILL_BORING; }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.DRILL_BORING; }

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

	public static class Serializer implements BlockRecipeSerializer<DrillBoringBlockRecipe> {
		@Override
		public DrillBoringBlockRecipe fromJson(ResourceLocation id, JsonObject obj) {
			BlockRecipeIngredient input = BlockRecipeIngredient.fromJson(obj.get("input"));
			Block result = Registry.BLOCK.get(new ResourceLocation(obj.get("result").getAsString()));
			boolean obeyFacing = !obj.has("obey_facing_or_axis") || obj.get("obey_facing_or_axis").getAsBoolean();
			return new DrillBoringBlockRecipe(id, input, result, obeyFacing);
		}

		@Override
		public DrillBoringBlockRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			BlockRecipeIngredient input = BlockRecipeIngredient.fromNetwork(buf);
			Block result = Registry.BLOCK.get(buf.readResourceLocation());
			boolean obeyFacing = buf.readBoolean();
			return new DrillBoringBlockRecipe(id, input, result, obeyFacing);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, DrillBoringBlockRecipe recipe) {
			recipe.input.toNetwork(buf);
			buf.writeResourceLocation(Registry.BLOCK.getKey(recipe.result))
			.writeBoolean(recipe.obeyFacingOrAxis);
		}
	}

}
