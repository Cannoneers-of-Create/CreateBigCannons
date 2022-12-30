package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe.LayerPredicate;

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
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.STEEL_CANNON_BARREL.get())
		.save(cons);
		
		builder("built_up_steel_cannon_barrel")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL.get())
		.save(cons);
	
		builder("steel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.STEEL_CANNON_CHAMBER.get())
		.save(cons);
		
		builder("built_up_steel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER.get())
		.save(cons);
		
		builder("thick_steel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.LARGE_STEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.THICK_STEEL_CANNON_CHAMBER.get())
		.save(cons);
		
		builder("nethersteel_cannon_barrel")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.NETHERSTEEL_CANNON_BARREL.get())
		.save(cons);
		
		builder("built_up_nethersteel_cannon_barrel")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL.get())
		.save(cons);
	
		builder("nethersteel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);
		
		builder("built_up_nethersteel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);
		
		builder("thick_nethersteel_cannon_chamber")
		.addLayer()
			.withBlocks(CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.addLayer()
			.withBlocks(CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.endLayer()
		.result(CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER.get())
		.save(cons);
	}
	
	protected Builder builder(String name) {
		return new Builder(name);
	}
	
	private class Builder {
		private final ResourceLocation id;
		
		private Set<LayerPredicate> layers = new LinkedHashSet<>();
		private Block result = null;
		
		private Builder(String name) {
			this.id = new ResourceLocation(BuiltUpHeatingRecipeProvider.this.modid, name);
		}
		
		public LayerBuilder addLayer() {
			return new LayerBuilder(this);
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
	
	private static class LayerBuilder {
		private final Builder parent;
		private Set<Block> blocks = null;
		private TagKey<Block> tag = null;
		
		public LayerBuilder(Builder parent) {
			this.parent = parent;
		}
		
		public LayerBuilder withBlocks(Block... addBlocks) {
			if (addBlocks == null || addBlocks.length == 0) return this;
			if (this.blocks == null) this.blocks = new LinkedHashSet<>();
			for (Block add : addBlocks) {
				if (add != null) this.blocks.add(add);
			}
			return this;
		}
		
		public LayerBuilder tag(TagKey<Block> tag) {
			this.tag = tag;
			return this;
		}
		
		public Builder endLayer() {
			this.parent.layers.add((this.blocks == null || this.blocks.isEmpty()) && this.tag == null ? LayerPredicate.ANY : new LayerPredicate(this.blocks, this.tag));
			return this.parent;
		}
	}
	
	private static class Result implements FinishedBlockRecipe {
		private final ResourceLocation id;
		private final Set<LayerPredicate> layers;
		private final Block result;
		
		public Result(Set<LayerPredicate> layers, Block result, ResourceLocation id) {
			this.layers = layers;
			this.result = result;
			this.id = id;
		}
		
		@Override
		public void serializeRecipeData(JsonObject obj) {
			JsonArray layersArr = new JsonArray();
			this.layers.stream()
			.map(p -> {
				JsonObject obj1 = new JsonObject();
				p.serializeJson(obj1);
				return obj1;
			})
			.forEach(layersArr::add);
			obj.add("layers", layersArr);
			obj.addProperty("result", ForgeRegistries.BLOCKS.getKey(this.result).toString());
		}

		@Override public ResourceLocation getId() { return this.id; }
		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.BUILT_UP_HEATING.get(); }
	}

}
