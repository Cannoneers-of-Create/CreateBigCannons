package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CannonCastRecipeProvider extends BlockRecipeProvider {

	CannonCastRecipeProvider(PackOutput output) { this(CreateBigCannons.MOD_ID, output); }

	public CannonCastRecipeProvider(String modid, PackOutput output) {
		super(modid, output);
		this.info = CreateBigCannons.resource("cannon_casting");
	}

	@Override
	protected void registerRecipes(Consumer<FinishedBlockRecipe> cons) {
		builder("unbored_cast_iron_cannon_barrel")
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get())
		.save(cons);

		builder("unbored_cast_iron_cannon_chamber")
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get())
		.save(cons);

		builder("cast_iron_cannon_end")
		.castingShape(CannonCastShape.CANNON_END)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.CAST_IRON_CANNON_END.get())
		.save(cons);

		builder("unbored_cast_iron_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get())
		.save(cons);

		TagKey<Fluid> bronzeTag = AllTags.forgeFluidTag("molten_bronze");

		builder("unbored_bronze_cannon_barrel")
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get())
		.save(cons);

		builder("unbored_bronze_cannon_chamber")
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get())
		.save(cons);

		builder("bronze_cannon_end")
		.castingShape(CannonCastShape.CANNON_END)
		.ingredient(bronzeTag)
		.result(CBCBlocks.BRONZE_CANNON_END.get())
		.save(cons);

		builder("unbored_bronze_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get())
		.save(cons);

		TagKey<Fluid> steelTag = AllTags.forgeFluidTag("molten_steel");

		builder("unbored_very_small_steel_cannon_layer")
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_small_steel_cannon_layer")
		.castingShape(CannonCastShape.SMALL)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_medium_steel_cannon_layer")
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_large_steel_cannon_layer")
		.castingShape(CannonCastShape.LARGE)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_very_large_steel_cannon_layer")
		.castingShape(CannonCastShape.VERY_LARGE)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_steel_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get())
		.save(cons);

		builder("unbored_steel_screw_breech")
		.castingShape(CannonCastShape.SCREW_BREECH)
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get())
		.save(cons);

		builder("unbored_very_small_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.VERY_SMALL)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_small_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.SMALL)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_medium_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.MEDIUM)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_large_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.LARGE)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_very_large_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.VERY_LARGE)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.save(cons);

		builder("unbored_nethersteel_screw_breech")
		.castingShape(CannonCastShape.SCREW_BREECH)
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get())
		.save(cons);

		builder("unbored_cast_iron_autocannon_breech")
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.get())
		.save(cons);

		builder("unbored_cast_iron_autocannon_recoil_spring")
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get())
		.save(cons);

		builder("unbored_cast_iron_autocannon_barrel")
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.get())
		.save(cons);

		builder("unbored_bronze_autocannon_breech")
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(CBCFluids.MOLTEN_BRONZE.get())
		.result(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.get())
		.save(cons);

		builder("unbored_bronze_autocannon_recoil_spring")
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(CBCFluids.MOLTEN_BRONZE.get())
		.result(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.get())
		.save(cons);

		builder("unbored_bronze_autocannon_barrel")
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(CBCFluids.MOLTEN_BRONZE.get())
		.result(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.get())
		.save(cons);

		builder("unbored_steel_autocannon_breech")
		.castingShape(CannonCastShape.AUTOCANNON_BREECH)
		.ingredient(CBCFluids.MOLTEN_STEEL.get())
		.result(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.get())
		.save(cons);

		builder("unbored_steel_autocannon_recoil_spring")
		.castingShape(CannonCastShape.AUTOCANNON_RECOIL_SPRING)
		.ingredient(CBCFluids.MOLTEN_STEEL.get())
		.result(CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.get())
		.save(cons);

		builder("unbored_steel_autocannon_barrel")
		.castingShape(CannonCastShape.AUTOCANNON_BARREL)
		.ingredient(CBCFluids.MOLTEN_STEEL.get())
		.result(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.get())
		.save(cons);
	}

	protected Builder builder(String name) {
		return new Builder(name);
	}

	private class Builder {
		private final ResourceLocation id;

		private CannonCastShape shape = null;
		private FluidIngredient ingredient = null;
		private Block result = null;

		private Builder(String name) {
			this.id = new ResourceLocation(CannonCastRecipeProvider.this.modid, name);
		}

		public Builder castingShape(CannonCastShape shape) {
			this.shape = shape;
			return this;
		}

		public Builder ingredient(Fluid ingredient) {
			this.ingredient = IndexPlatform.fluidIngredientFrom(ingredient, 1);
			return this;
		}

		public Builder ingredient(TagKey<Fluid> ingredient) {
			this.ingredient = IndexPlatform.fluidIngredientFrom(ingredient, 1);
			return this;
		}

		public Builder result(Block result) {
			this.result = result;
			return this;
		}

		public void save(Consumer<FinishedBlockRecipe> cons) {
			Objects.requireNonNull(this.shape, "Recipe " + this.id + " has no casting shape specified");
			Objects.requireNonNull(this.ingredient, "Recipe " + this.id + " has no fluid ingredient specified");
			Objects.requireNonNull(this.result, "Recipe " + this.id + " has no result specified");
			cons.accept(new Result(this.shape, this.ingredient, this.result, this.id));
		}
	}

	private static class Result implements FinishedBlockRecipe {
		private final ResourceLocation id;
		private final CannonCastShape shape;
		private final FluidIngredient ingredient;
		private final Block result;

		public Result(CannonCastShape shape, FluidIngredient ingredient, Block result, ResourceLocation id) {
			this.shape = shape;
			this.ingredient = ingredient;
			this.result = result;
			this.id = id;
		}

		@Override
		public void serializeRecipeData(JsonObject obj) {
			obj.addProperty("cast_shape", CBCRegistries.cannonCastShapes().getKey(this.shape).toString());
			obj.add("fluid", this.ingredient.serialize());
			obj.addProperty("result", BuiltInRegistries.BLOCK.getKey(this.result).toString());
		}

		@Override public ResourceLocation id() { return this.id; }
		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.CANNON_CASTING; }
	}

}
