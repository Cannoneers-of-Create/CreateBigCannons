package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class DrillBoringRecipeProvider extends BlockRecipeProvider {

	DrillBoringRecipeProvider(PackOutput output) { this(CreateBigCannons.MOD_ID, output); }

	public DrillBoringRecipeProvider(String modid, PackOutput output) {
		super(modid, output);
	}

	@Override
	protected void registerRecipes(Consumer<FinishedBlockRecipe> cons) {
		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get(), CBCBlocks.CAST_IRON_CANNON_BARREL.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get(), CBCBlocks.CAST_IRON_CANNON_CHAMBER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get(), CBCBlocks.BRONZE_CANNON_BARREL.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get(), CBCBlocks.BRONZE_CANNON_CHAMBER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get(), CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get(), CBCBlocks.SMALL_STEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get(), CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get(), CBCBlocks.LARGE_STEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get(), CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get(), CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.get(), CBCBlocks.CAST_IRON_AUTOCANNON_BARREL.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.get(), CBCBlocks.BRONZE_AUTOCANNON_BARREL.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH.get()));

		cons.accept(recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.get(), CBCBlocks.STEEL_AUTOCANNON_BARREL.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING.get()));
		cons.accept(recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH.get()));
	}

	protected Result recipe(Block input, Block result) {
		return recipe(BuiltInRegistries.BLOCK.getKey(result).getPath(), input, result);
	}

	protected Result recipe(String path, Block input, Block result) {
		return recipe(path, input, result, true);
	}

	protected Result recipe(String path, Block input, Block result, boolean obeyFacing) {
		return new Result(BlockRecipeIngredient.of(input), result, new ResourceLocation(this.modid, path), obeyFacing);
	}

	protected Result recipe(TagKey<Block> input, Block result) {
		return recipe(BuiltInRegistries.BLOCK.getKey(result).getPath(), input, result);
	}

	protected Result recipe(String path, TagKey<Block> input, Block result) {
		return recipe(path, input, result, true);
	}

	protected Result recipe(String path, TagKey<Block> input, Block result, boolean obeyFacing) {
		return new Result(BlockRecipeIngredient.of(input), result, new ResourceLocation(this.modid, path), obeyFacing);
	}

	private record Result(BlockRecipeIngredient input, Block result, ResourceLocation id, boolean obeyFacing) implements FinishedBlockRecipe {
		@Override
		public void serializeRecipeData(JsonObject obj) {
			obj.addProperty("input", this.input.stringForSerialization());
			obj.addProperty("result", BuiltInRegistries.BLOCK.getKey(this.result).toString());
			obj.addProperty("obey_facing_or_axis", this.obeyFacing);
		}

		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.DRILL_BORING; }
	}

}
