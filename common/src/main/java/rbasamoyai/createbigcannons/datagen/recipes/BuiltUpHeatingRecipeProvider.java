package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCUtils;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class BuiltUpHeatingRecipeProvider extends BlockRecipeProvider {

	BuiltUpHeatingRecipeProvider(DataGenerator gen) {
		this(CreateBigCannons.MOD_ID, gen);
	}

	public BuiltUpHeatingRecipeProvider(String modid, DataGenerator gen) {
		super(modid, gen);
		this.info = CreateBigCannons.resource("built_up_heating");
	}

	@Override
	protected void registerRecipes(Consumer<FinishedBlockRecipe> cons) {
		builder("steel_cannon_barrel")
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.result(CBCBlocks.STEEL_CANNON_BARREL.get())
		.save(cons);

		builder("built_up_steel_cannon_barrel")
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.result(CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL.get())
		.save(cons);

		builder("steel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.result(CBCBlocks.STEEL_CANNON_CHAMBER.get())
		.save(cons);

		builder("built_up_steel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.result(CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER.get())
		.save(cons);

		builder("thick_steel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.get())
		.result(CBCBlocks.THICK_STEEL_CANNON_CHAMBER.get())
		.save(cons);

		builder("nethersteel_cannon_barrel")
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.result(CBCBlocks.NETHERSTEEL_CANNON_BARREL.get())
		.save(cons);

		builder("built_up_nethersteel_cannon_barrel")
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.result(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL.get())
		.save(cons);

		builder("nethersteel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.result(CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);

		builder("built_up_nethersteel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.result(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);

		builder("thick_nethersteel_cannon_chamber")
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.result(CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);
	}

	protected Builder builder(String name) {
		return new Builder(name);
	}

	private class Builder {
		private final ResourceLocation id;

		private Set<BlockRecipeIngredient> layers = new LinkedHashSet<>();
		private Block result = null;

		private Builder(String name) {
			this.id = CBCUtils.location(BuiltUpHeatingRecipeProvider.this.modid, name);
		}

		public Builder addLayer(Block block) {
			this.layers.add(BlockRecipeIngredient.of(block));
			return this;
		}

		public Builder addLayer(TagKey<Block> tag) {
			this.layers.add(BlockRecipeIngredient.of(tag));
			return this;
		}

		public Builder result(Block result) {
			this.result = result;
			return this;
		}

		public void save(Consumer<FinishedBlockRecipe> cons) {
			if (this.layers.isEmpty()) throw new IllegalStateException("Recipe " + this.id + " has no layers specified");
			Objects.requireNonNull(this.result, "Recipe " + this.id + " has no result specified");
			cons.accept(new Result(this.layers, this.result, this.id));
		}
	}

	private record Result(Set<BlockRecipeIngredient> layers, Block result, ResourceLocation id) implements FinishedBlockRecipe {
		@Override
		public void serializeRecipeData(JsonObject obj) {
			JsonArray layersArr = new JsonArray();
			this.layers.stream()
					.map(BlockRecipeIngredient::stringForSerialization)
					.forEach(layersArr::add);
			obj.add("layers", layersArr);
			obj.addProperty("result", Registry.BLOCK.getKey(this.result).toString());
		}

		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.BUILT_UP_HEATING; }
	}

}
