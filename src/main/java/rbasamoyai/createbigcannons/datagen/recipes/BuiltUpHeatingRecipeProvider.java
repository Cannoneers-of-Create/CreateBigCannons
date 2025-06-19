package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class BuiltUpHeatingRecipeProvider extends BlockRecipeProvider {

	public BuiltUpHeatingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
		this.info = CreateBigCannons.resource("built_up_heating");
	}

	@Override
	protected void registerRecipes(BiConsumer<ResourceLocation, BlockRecipe> cons) {
		builder(CBCBlocks.STEEL_CANNON_BARREL.get())
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL.get())
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.STEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.THICK_STEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.NETHERSTEEL_CANNON_BARREL.get())
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL.get())
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder(CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER.get())
		.addLayer(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.addLayer(CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);
	}

	protected Builder builder(Block result) { return new Builder(result); }

	protected static class Builder {
		private final Set<BlockRecipeIngredient> layers = new LinkedHashSet<>();
        private final List<BlockRecipeIngredient> layersList = new ArrayList<>();
		private final Block result;

		private Builder(Block result) {
            this.result = result;
		}

		public Builder addLayer(Block block) {
            BlockRecipeIngredient ingredient = BlockRecipeIngredient.of(block);
            if (!this.layers.add(ingredient))
                throw new IllegalStateException("Cannot add the same layer twice: " + CBCRegistryUtils.getBlockLocation(block));
			this.layers.add(ingredient);
            this.layersList.add(ingredient);
			return this;
		}

		public Builder addLayer(TagKey<Block> tag) {
            BlockRecipeIngredient ingredient = BlockRecipeIngredient.of(tag);
            if (!this.layers.add(ingredient))
                throw new IllegalStateException("Cannot add the same layer twice: #" + tag.location());
            this.layers.add(ingredient);
            this.layersList.add(ingredient);
			return this;
		}

        public void save(BiConsumer<ResourceLocation, BlockRecipe> cons) {
            this.save(cons, CBCRegistryUtils.getBlockLocation(this.result));
        }

		public void save(BiConsumer<ResourceLocation, BlockRecipe> cons, ResourceLocation id) {
			if (this.layers.isEmpty())
                throw new IllegalStateException("Recipe " + id + " has no layers specified");
			Objects.requireNonNull(this.result, "Recipe " + id + " has no result specified");
			cons.accept(id, new BuiltUpHeatingRecipe(this.layersList, this.result));
		}
	}

}
