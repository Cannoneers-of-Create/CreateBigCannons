package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CBCFluids;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.CBCRecipeTypes;

public class MeltingRecipeProvider extends ProcessingRecipeGen {

	public MeltingRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override protected IRecipeTypeInfo getRecipeType() { return CBCRecipeTypes.MELTING; }
	
	GeneratedRecipe
	
	MELT_CAST_IRON_INGOT = create(CreateBigCannons.resource("melt_cast_iron_ingot"), b -> b.require(CBCTags.ItemCBC.INGOT_CAST_IRON)
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_CAST_IRON.get(), 144)),
	
	MELT_CAST_IRON_NUGGET = create(CreateBigCannons.resource("melt_cast_iron_nugget"), b -> b.require(CBCTags.ItemCBC.NUGGET_CAST_IRON)
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_CAST_IRON.get(), 16)),

	MELT_BRONZE_INGOT = create(CreateBigCannons.resource("melt_bronze_ingot"), b -> b.require(CBCTags.ItemCBC.INGOT_BRONZE)
			.duration(180)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_BRONZE.get(), 144)),
	
	MELT_BRONZE_NUGGET = create(CreateBigCannons.resource("melt_bronze_nugget"), b -> b.require(CBCTags.ItemCBC.NUGGET_BRONZE)
			.duration(20)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCFluids.MOLTEN_BRONZE.get(), 16));

}
