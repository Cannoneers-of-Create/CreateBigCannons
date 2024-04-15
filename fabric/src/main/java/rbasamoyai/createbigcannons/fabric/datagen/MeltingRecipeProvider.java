package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class MeltingRecipeProvider extends ProcessingRecipeGen {

	public MeltingRecipeProvider(DataGenerator generator) {
		super(IndexPlatform.castGen(generator));
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return CBCRecipeTypes.MELTING;
	}

	GeneratedRecipe

		MELT_CAST_IRON_BLOCK = create(CreateBigCannons.resource("melt_cast_iron_block"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.BLOCK_CAST_IRON))
		.require(CBCTags.CBCItemTags.BLOCK_CAST_IRON)
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(810))),

	MELT_CAST_IRON_INGOT = create(CreateBigCannons.resource("melt_cast_iron_ingot"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.INGOT_CAST_IRON))
		.require(CBCTags.CBCItemTags.INGOT_CAST_IRON)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(90))),

	MELT_CAST_IRON_NUGGET = create(CreateBigCannons.resource("melt_cast_iron_nugget"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.NUGGET_CAST_IRON))
		.require(CBCTags.CBCItemTags.NUGGET_CAST_IRON)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(10))),

	MELT_BRONZE_BLOCK = create(CreateBigCannons.resource("melt_bronze_block"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.BLOCK_BRONZE))
		.require(CBCTags.CBCItemTags.BLOCK_BRONZE)
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(810))),

	MELT_BRONZE_INGOT = create(CreateBigCannons.resource("melt_bronze_ingot"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.INGOT_BRONZE))
		.require(CBCTags.CBCItemTags.INGOT_BRONZE)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(90))),

	MELT_BRONZE_NUGGET = create(CreateBigCannons.resource("melt_bronze_nugget"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.NUGGET_BRONZE))
		.require(CBCTags.CBCItemTags.NUGGET_BRONZE)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(10))),

	MELT_STEEL_BLOCK = create(CreateBigCannons.resource("melt_steel_block"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.BLOCK_STEEL))
		.require(CBCTags.CBCItemTags.BLOCK_STEEL)
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(810))),

	MELT_STEEL_INGOT = create(CreateBigCannons.resource("melt_steel_ingot"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.INGOT_STEEL))
		.require(CBCTags.CBCItemTags.INGOT_STEEL)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(90))),

	MELT_STEEL_NUGGET = create(CreateBigCannons.resource("melt_steel_nugget"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.NUGGET_STEEL))
		.require(CBCTags.CBCItemTags.NUGGET_STEEL)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(10))),

	MELT_NETHERSTEEL_BLOCK = create(CreateBigCannons.resource("melt_nethersteel_block"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.BLOCK_NETHERSTEEL))
		.require(CBCTags.CBCItemTags.BLOCK_NETHERSTEEL)
		.duration(1620)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(810))),

	MELT_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("melt_nethersteel_ingot"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.INGOT_NETHERSTEEL))
		.require(CBCTags.CBCItemTags.INGOT_NETHERSTEEL)
		.duration(180)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(90))),

	MELT_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("melt_nethersteel_nugget"), b -> b
		.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.CBCItemTags.NUGGET_NETHERSTEEL))
		.require(CBCTags.CBCItemTags.NUGGET_NETHERSTEEL)
		.duration(20)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(10)));

}
