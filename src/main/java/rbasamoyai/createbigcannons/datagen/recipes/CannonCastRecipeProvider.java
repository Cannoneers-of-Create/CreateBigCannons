package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCFluids;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonCastRecipeProvider extends BlockRecipeProvider {
	
	CannonCastRecipeProvider(DataGenerator gen) {
		this(CreateBigCannons.MOD_ID, gen);
	}
	
	public CannonCastRecipeProvider(String modid, DataGenerator gen) {
		super(modid, gen);
		this.info = CreateBigCannons.resource("cannon_casting");
	}
	
	@Override
	protected void registerRecipes(Consumer<FinishedBlockRecipe> cons) {
		builder("unbored_cast_iron_cannon_barrel")
		.castingShape(CannonCastShape.VERY_SMALL.get())
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get())
		.save(cons);
		
		builder("unbored_cast_iron_cannon_chamber")
		.castingShape(CannonCastShape.MEDIUM.get())
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get())
		.castingTime(1800)
		.save(cons);
		
		builder("cast_iron_cannon_end")
		.castingShape(CannonCastShape.CANNON_END.get())
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.CAST_IRON_CANNON_END.get())
		.castingTime(1500)
		.save(cons);
		
		builder("unbored_cast_iron_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH.get())
		.ingredient(CBCFluids.MOLTEN_CAST_IRON.get())
		.result(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get())
		.castingTime(1500)
		.save(cons);
		
		TagKey<Fluid> bronzeTag = AllTags.forgeFluidTag("molten_bronze");
		
		builder("unbored_bronze_cannon_barrel")
		.castingShape(CannonCastShape.VERY_SMALL.get())
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get())
		.save(cons);
		
		builder("unbored_bronze_cannon_chamber")
		.castingShape(CannonCastShape.MEDIUM.get())
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get())
		.castingTime(1800)
		.save(cons);
		
		builder("bronze_cannon_end")
		.castingShape(CannonCastShape.CANNON_END.get())
		.ingredient(bronzeTag)
		.result(CBCBlocks.BRONZE_CANNON_END.get())
		.castingTime(1500)
		.save(cons);
		
		builder("unbored_bronze_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH.get())
		.ingredient(bronzeTag)
		.result(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get())
		.castingTime(1500)
		.save(cons);
		
		TagKey<Fluid> steelTag = AllTags.forgeFluidTag("molten_steel");
		
		builder("unbored_very_small_steel_cannon_layer")
		.castingShape(CannonCastShape.VERY_SMALL.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get())
		.castingTime(2400)
		.save(cons);
		
		builder("unbored_small_steel_cannon_layer")
		.castingShape(CannonCastShape.SMALL.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get())
		.castingTime(3000)
		.save(cons);
		
		builder("unbored_medium_steel_cannon_layer")
		.castingShape(CannonCastShape.MEDIUM.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get())
		.castingTime(3600)
		.save(cons);
		
		builder("unbored_large_steel_cannon_layer")
		.castingShape(CannonCastShape.LARGE.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get())
		.castingTime(4200)
		.save(cons);
		
		builder("unbored_very_large_steel_cannon_layer")
		.castingShape(CannonCastShape.VERY_LARGE.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get())
		.castingTime(4800)
		.save(cons);
		
		builder("unbored_steel_sliding_breech")
		.castingShape(CannonCastShape.SLIDING_BREECH.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get())
		.castingTime(3000)
		.save(cons);
		
		builder("unbored_steel_screw_breech")
		.castingShape(CannonCastShape.SCREW_BREECH.get())
		.ingredient(steelTag)
		.result(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get())
		.castingTime(3000)
		.save(cons);
		
		builder("unbored_very_small_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.VERY_SMALL.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.castingTime(2400)
		.save(cons);
		
		builder("unbored_small_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.SMALL.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get())
		.castingTime(3000)
		.save(cons);
		
		builder("unbored_medium_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.MEDIUM.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get())
		.castingTime(3600)
		.save(cons);
		
		builder("unbored_large_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.LARGE.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.castingTime(4200)
		.save(cons);
		
		builder("unbored_very_large_nethersteel_cannon_layer")
		.castingShape(CannonCastShape.VERY_LARGE.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get())
		.castingTime(4800)
		.save(cons);
		
		builder("unbored_nethersteel_screw_breech")
		.castingShape(CannonCastShape.SCREW_BREECH.get())
		.ingredient(CBCFluids.MOLTEN_NETHERSTEEL.get())
		.result(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get())
		.castingTime(3000)
		.save(cons);
	}
	
	protected Builder builder(String name) {
		return new Builder(name);
	}
	
	private class Builder {
		private final ResourceLocation id;
		
		private CannonCastShape shape = null;
		private FluidIngredient ingredient = null;
		private int castingTime = 1200;
		private Block result = null;
		
		private Builder(String name) {
			this.id = new ResourceLocation(CannonCastRecipeProvider.this.modid, name);
		}
		
		public Builder castingShape(CannonCastShape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder ingredient(Fluid ingredient) {
			this.ingredient = FluidIngredient.fromFluid(ingredient, 1);
			return this;
		}
		
		public Builder ingredient(TagKey<Fluid> ingredient) {
			this.ingredient = FluidIngredient.fromTag(ingredient, 1);
			return this;
		}
		
		public Builder castingTime(int castingTime) {
			this.castingTime = castingTime;
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
			cons.accept(new Result(this.shape, this.ingredient, this.result, this.castingTime, this.id));
		}
	}
	
	private static class Result implements FinishedBlockRecipe {
		private final ResourceLocation id;
		private final CannonCastShape shape;
		private final FluidIngredient ingredient;
		private final int castingTime;
		private final Block result;
		
		public Result(CannonCastShape shape, FluidIngredient ingredient, Block result, int castingTime, ResourceLocation id) {
			this.shape = shape;
			this.ingredient = ingredient;
			this.result = result;
			this.castingTime = castingTime;
			this.id = id;
		}
		
		@Override
		public void serializeRecipeData(JsonObject obj) {
			obj.addProperty("cast_shape", CBCRegistries.CANNON_CAST_SHAPES.get().getKey(this.shape).toString());
			obj.add("fluid", this.ingredient.serialize());
			obj.addProperty("casting_time", this.castingTime);
			obj.addProperty("result", ForgeRegistries.BLOCKS.getKey(this.result).toString());
		}

		@Override public ResourceLocation getId() { return this.id; }
		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.CANNON_CASTING.get(); }
	}

}
