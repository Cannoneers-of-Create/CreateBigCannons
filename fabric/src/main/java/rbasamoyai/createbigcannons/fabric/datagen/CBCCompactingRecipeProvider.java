package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.ItemTags;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenCommon;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCCompactingRecipeProvider extends ProcessingRecipeGen {

	public CBCCompactingRecipeProvider(FabricDataOutput output) {
		super(output, CreateBigCannons.MOD_ID);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.COMPACTING;
	}

	GeneratedRecipe

		PACKED_GUNPOWDER = create(CreateBigCannons.resource("packed_gunpowder"), b -> b.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.output(CBCItems.PACKED_GUNPOWDER.get())),

    FORGE_CAST_IRON_BLOCK = create(CreateBigCannons.resource("forge_cast_iron_block"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 810 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCBlocks.CAST_IRON_BLOCK)),

	FORGE_CAST_IRON_INGOT = create(CreateBigCannons.resource("forge_cast_iron_ingot"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 90 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.output(CBCItems.CAST_IRON_INGOT.get())),

	FORGE_CAST_IRON_NUGGET = create(CreateBigCannons.resource("forge_cast_iron_nugget"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 10 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.output(CBCItems.CAST_IRON_NUGGET.get())),

    FORGE_BRONZE_BLOCK = create(CreateBigCannons.resource("forge_bronze_block"), b -> b.require(AllTags.forgeFluidTag("molten_bronze"), 810 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCBlocks.BRONZE_BLOCK)),

    FORGE_BRONZE_INGOT = create(CreateBigCannons.resource("forge_bronze_ingot"), b -> b.require(AllTags.forgeFluidTag("molten_bronze"), 90 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCItems.BRONZE_INGOT)),

    FORGE_BRONZE_NUGGET = create(CreateBigCannons.resource("forge_bronze_nugget"), b -> b.require(AllTags.forgeFluidTag("molten_bronze"), 10 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCItems.BRONZE_SCRAP)),

    FORGE_STEEL_BLOCK = create(CreateBigCannons.resource("forge_steel_block"), b -> b.require(AllTags.forgeFluidTag("molten_steel"), 810 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCBlocks.STEEL_BLOCK)),

    FORGE_STEEL_INGOT = create(CreateBigCannons.resource("forge_steel_ingot"), b -> b.require(AllTags.forgeFluidTag("molten_steel"), 90 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCItems.STEEL_INGOT)),

    FORGE_STEEL_NUGGET = create(CreateBigCannons.resource("forge_steel_nugget"), b -> b.require(AllTags.forgeFluidTag("molten_steel"), 10 * CBCDatagenCommon.FLUID_MULTIPLIER)
        .output(CBCItems.STEEL_SCRAP)),

	FORGE_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("forge_nethersteel_ingot"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 90 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.output(CBCItems.NETHERSTEEL_INGOT.get())),

	FORGE_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("forge_nethersteel_nugget"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 10 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.output(CBCItems.NETHERSTEEL_NUGGET.get())),

	// The following are adapted from Create Deco
	IRON_TO_CAST_IRON_INGOT = create(CreateBigCannons.resource("iron_to_cast_iron_ingot"), b -> b.require(CommonMetal.IRON.ingots)
        .require(ItemTags.COALS)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCItems.CAST_IRON_INGOT.get())),

	IRON_TO_CAST_IRON_BLOCK = create(CreateBigCannons.resource("iron_to_cast_iron_block"), b -> b.require(CommonMetal.IRON.storageBlocks.items())
        .require(Tags.Items.STORAGE_BLOCKS_COAL)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCBlocks.CAST_IRON_BLOCK.get())),

	PACKED_GUNCOTTON = create(CreateBigCannons.resource("packed_guncotton"), b -> b.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.output(CBCItems.PACKED_GUNCOTTON.get()));

}
