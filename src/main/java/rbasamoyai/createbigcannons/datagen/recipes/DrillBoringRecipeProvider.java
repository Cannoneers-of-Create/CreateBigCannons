package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class DrillBoringRecipeProvider extends BlockRecipeProvider {

	public DrillBoringRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void registerRecipes(BiConsumer<ResourceLocation, BlockRecipe> cons) {
		recipe(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get(), CBCBlocks.CAST_IRON_CANNON_BARREL.get(), cons);
		recipe(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get(), CBCBlocks.CAST_IRON_CANNON_CHAMBER.get(), cons);
		recipe(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get(), CBCBlocks.BRONZE_CANNON_BARREL.get(), cons);
		recipe(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get(), CBCBlocks.BRONZE_CANNON_CHAMBER.get(), cons);
		recipe(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get(), CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get(), CBCBlocks.SMALL_STEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get(), CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get(), CBCBlocks.LARGE_STEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get(), CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH.get(), cons);
		recipe(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get(), CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get(), cons);
		recipe(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get(), CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.get(), CBCBlocks.CAST_IRON_AUTOCANNON_BARREL.get(), cons);
		recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get(), cons);
		recipe(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.get(), CBCBlocks.BRONZE_AUTOCANNON_BARREL.get(), cons);
		recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING.get(), cons);
		recipe(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH.get(), cons);

		recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.get(), CBCBlocks.STEEL_AUTOCANNON_BARREL.get(), cons);
		recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.get(), CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING.get(), cons);
		recipe(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.get(), CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH.get(), cons);
	}

	protected void recipe(Block input, Block result, BiConsumer<ResourceLocation, BlockRecipe> cons) {
		recipe(CBCRegistryUtils.getBlockLocation(result), input, result, cons);
	}

	protected void recipe(ResourceLocation id, Block input, Block result, BiConsumer<ResourceLocation, BlockRecipe> cons) {
		recipe(id, input, result, true, cons);
	}

	protected void recipe(ResourceLocation id, Block input, Block result, boolean obeyFacing, BiConsumer<ResourceLocation, BlockRecipe> cons) {
        cons.accept(id, new DrillBoringBlockRecipe(BlockRecipeIngredient.of(input), result, obeyFacing));
	}

	protected void recipe(TagKey<Block> input, Block result, BiConsumer<ResourceLocation, BlockRecipe> cons) {
		recipe(CBCRegistryUtils.getBlockLocation(result), input, result, cons);
	}

	protected void recipe(ResourceLocation id, TagKey<Block> input, Block result, BiConsumer<ResourceLocation, BlockRecipe> cons) {
		recipe(id, input, result, true, cons);
	}

	protected void recipe(ResourceLocation id, TagKey<Block> input, Block result, boolean obeyFacing, BiConsumer<ResourceLocation, BlockRecipe> cons) {
        cons.accept(id, new DrillBoringBlockRecipe(BlockRecipeIngredient.of(input), result, obeyFacing));
	}

}
