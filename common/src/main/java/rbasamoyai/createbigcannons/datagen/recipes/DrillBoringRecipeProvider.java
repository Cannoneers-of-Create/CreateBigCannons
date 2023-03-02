package rbasamoyai.createbigcannons.datagen.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;

import java.util.function.Consumer;

public class DrillBoringRecipeProvider extends BlockRecipeProvider {

	DrillBoringRecipeProvider(DataGenerator gen) { this(CreateBigCannons.MOD_ID, gen); }

	public DrillBoringRecipeProvider(String modid, DataGenerator gen) {
		super(modid, gen);
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
		return recipe(Registry.BLOCK.getKey(result).getPath(), input, result);
	}

	protected Result recipe(String path, Block input, Block result) {
		return new Result(BlockRecipeIngredient.of(input), result, new ResourceLocation(this.modid, path));
	}

	protected Result recipe(TagKey<Block> input, Block result) {
		return recipe(Registry.BLOCK.getKey(result).getPath(), input, result);
	}

	protected Result recipe(String path, TagKey<Block> input, Block result) {
		return new Result(BlockRecipeIngredient.of(input), result, new ResourceLocation(this.modid, path));
	}

	private record Result(BlockRecipeIngredient input, Block result, ResourceLocation id) implements FinishedBlockRecipe {
		@Override
		public void serializeRecipeData(JsonObject obj) {
			obj.addProperty("input", this.input.stringForSerialization());
			obj.addProperty("result", ForgeRegistries.BLOCKS.getKey(this.result).toString());
		}

		@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.DRILL_BORING.get(); }
	}

}
