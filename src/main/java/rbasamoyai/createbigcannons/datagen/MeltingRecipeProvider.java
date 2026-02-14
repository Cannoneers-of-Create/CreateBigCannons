package rbasamoyai.createbigcannons.datagen;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.StandardProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class MeltingRecipeProvider extends StandardProcessingRecipeGen<MeltingRecipe> {

	public MeltingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, CreateBigCannons.MOD_ID);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return CBCRecipeTypes.MELTING;
	}

	GeneratedRecipe

		MELT_CAST_IRON_BLOCK = create(CreateBigCannons.resource("melt_cast_iron_block"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.CAST_IRON.storageBlocks.items()))
		.require(CBCCommonMetal.CAST_IRON.storageBlocks.items())
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), 810)),

	MELT_CAST_IRON_INGOT = create(CreateBigCannons.resource("melt_cast_iron_ingot"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.CAST_IRON.ingots))
		.require(CBCCommonMetal.CAST_IRON.ingots)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), 90)),

	MELT_CAST_IRON_NUGGET = create(CreateBigCannons.resource("melt_cast_iron_nugget"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.CAST_IRON.nuggets))
		.require(CBCCommonMetal.CAST_IRON.nuggets)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), 10)),

	MELT_BRONZE_BLOCK = create(CreateBigCannons.resource("melt_bronze_block"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.BRONZE.storageBlocks.items()))
		.require(CBCCommonMetal.BRONZE.storageBlocks.items())
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), 810)),

	MELT_BRONZE_INGOT = create(CreateBigCannons.resource("melt_bronze_ingot"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.BRONZE.ingots))
		.require(CBCCommonMetal.BRONZE.ingots)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), 90)),

	MELT_BRONZE_NUGGET = create(CreateBigCannons.resource("melt_bronze_nugget"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.BRONZE.nuggets))
		.require(CBCCommonMetal.BRONZE.nuggets)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), 10)),

	MELT_STEEL_BLOCK = create(CreateBigCannons.resource("melt_steel_block"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CommonMetal.STEEL.storageBlocks.items()))
		.require(CommonMetal.STEEL.storageBlocks.items())
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), 810)),

	MELT_STEEL_INGOT = create(CreateBigCannons.resource("melt_steel_ingot"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CommonMetal.STEEL.ingots))
		.require(CommonMetal.STEEL.ingots)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), 90)),

	MELT_STEEL_NUGGET = create(CreateBigCannons.resource("melt_steel_nugget"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CommonMetal.STEEL.nuggets))
		.require(CommonMetal.STEEL.nuggets)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), 10)),

	MELT_NETHERSTEEL_BLOCK = create(CreateBigCannons.resource("melt_nethersteel_block"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.NETHERSTEEL.storageBlocks.items()))
		.require(CBCCommonMetal.NETHERSTEEL.storageBlocks.items())
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), 810)),

	MELT_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("melt_nethersteel_ingot"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.NETHERSTEEL.ingots))
		.require(CBCCommonMetal.NETHERSTEEL.ingots)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), 90)),

	MELT_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("melt_nethersteel_nugget"), b -> b
		.withCondition(CBCUtils.itemTagsPopulated(CBCCommonMetal.NETHERSTEEL.nuggets))
		.require(CBCCommonMetal.NETHERSTEEL.nuggets)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), 10));

}
