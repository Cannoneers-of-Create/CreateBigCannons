package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.*;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class MeltingRecipeProvider extends ProcessingRecipeGen {

	public MeltingRecipeProvider(DataGenerator generator) {
		super(IndexPlatform.castGen(generator));
	}

	@Override protected IRecipeTypeInfo getRecipeType() { return CBCRecipeTypes.MELTING; }
	
	GeneratedRecipe
	
	MELT_CAST_IRON_BLOCK = create(CreateBigCannons.resource("melt_cast_iron_block"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.BLOCK_CAST_IRON))
			.require(CBCTags.ItemCBC.BLOCK_CAST_IRON)
			.duration(1620)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(810))),
	
	MELT_CAST_IRON_INGOT = create(CreateBigCannons.resource("melt_cast_iron_ingot"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.INGOT_CAST_IRON))
			.require(CBCTags.ItemCBC.INGOT_CAST_IRON)
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(90))),
	
	MELT_CAST_IRON_NUGGET = create(CreateBigCannons.resource("melt_cast_iron_nugget"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.NUGGET_CAST_IRON))
			.require(CBCTags.ItemCBC.NUGGET_CAST_IRON)
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_CAST_IRON.get().getSource(), IndexPlatform.convertFluid(10))),

	MELT_BRONZE_BLOCK = create(CreateBigCannons.resource("melt_bronze_block"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.BLOCK_BRONZE))
			.require(CBCTags.ItemCBC.BLOCK_BRONZE)
			.duration(1620)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(810))),
			
	MELT_BRONZE_INGOT = create(CreateBigCannons.resource("melt_bronze_ingot"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.INGOT_BRONZE))
			.require(CBCTags.ItemCBC.INGOT_BRONZE)
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(90))),
	
	MELT_BRONZE_NUGGET = create(CreateBigCannons.resource("melt_bronze_nugget"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.NUGGET_BRONZE))
			.require(CBCTags.ItemCBC.NUGGET_BRONZE)
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_BRONZE.get().getSource(), IndexPlatform.convertFluid(10))),

	MELT_STEEL_BLOCK = create(CreateBigCannons.resource("melt_steel_block"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.BLOCK_STEEL))
			.require(CBCTags.ItemCBC.BLOCK_STEEL)
			.duration(1620)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(810))),
	
	MELT_STEEL_INGOT = create(CreateBigCannons.resource("melt_steel_ingot"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.INGOT_STEEL))
			.require(CBCTags.ItemCBC.INGOT_STEEL)
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(90))),
	
	MELT_STEEL_NUGGET = create(CreateBigCannons.resource("melt_steel_nugget"), b -> b
			.withCondition(DefaultResourceConditions.itemTagsPopulated(CBCTags.ItemCBC.NUGGET_STEEL))
			.require(CBCTags.ItemCBC.NUGGET_STEEL)
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_STEEL.get().getSource(), IndexPlatform.convertFluid(10))),
	
	MELT_NETHERSTEEL_BLOCK = create(CreateBigCannons.resource("melt_nethersteel_block"), b -> b.require(CBCBlocks.NETHERSTEEL_BLOCK.get())
			.duration(1620)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(810))),
	
	MELT_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("melt_nethersteel_ingot"), b -> b.require(CBCItems.NETHERSTEEL_INGOT.get())
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(90))),
	
	MELT_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("melt_nethersteel_nugget"), b -> b.require(CBCItems.NETHERSTEEL_NUGGET.get())
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_NETHERSTEEL.get().getSource(), IndexPlatform.convertFluid(10)));
	
}
