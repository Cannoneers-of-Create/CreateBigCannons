package rbasamoyai.createbigcannons.datagen.forge;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenCommon;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCMixingRecipeProvider extends ProcessingRecipeGen {

	public CBCMixingRecipeProvider(PackOutput output) {
		super(output, CreateBigCannons.MOD_ID);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MIXING;
	}

    GeneratedRecipe

        ALLOY_NETHERSTEEL_CAST_IRON = create(CreateBigCannons.resource("alloy_nethersteel_cast_iron"), b -> nethersteelAlloy(b, CBCItemTags.INGOT_CAST_IRON, 8, 8)),

    ALLOY_NETHERSTEEL_STEEL = create(CreateBigCannons.resource("alloy_nethersteel_steel"), b -> nethersteelAlloy(b, CommonMetal.STEEL.ingots, 4, 8)),

    // Adapted from Create: Alloyed.
    ALLOY_BRONZE_TINLESS = create(CreateBigCannons.resource("alloy_bronze_tinless"), b -> b.require(CommonMetal.COPPER.ingots)
        .require(CommonMetal.ZINC.ingots)
        .require(AllItems.CINDER_FLOUR)
        .requiresHeat(HeatCondition.HEATED)
        .output(CBCItems.BRONZE_INGOT.get(), 2)),

    ALLOY_BRONZE_TIN = create(CreateBigCannons.resource("alloy_bronze_tin"), b -> b.withCondition(new NotCondition(new TagEmptyCondition(CommonMetal.TIN.ingots.location())))
        .require(CommonMetal.COPPER.ingots)
        .require(CommonMetal.TIN.ingots)
        .requiresHeat(HeatCondition.HEATED)
        .output(CBCItems.BRONZE_INGOT.get(), 2)),

    ALLOY_BRONZE_BRASS = create(CreateBigCannons.resource("alloy_bronze_brass"), b -> b.require(CommonMetal.BRASS.ingots)
        .require(CommonMetal.BRASS.ingots)
        .require(AllItems.CINDER_FLOUR)
        .requiresHeat(HeatCondition.HEATED)
        .output(CBCItems.BRONZE_INGOT.get(), 2)),

    ALLOY_STEEL = create(CreateBigCannons.resource("alloy_steel"), b -> b.require(CommonMetal.IRON.ingots)
        .require(CommonMetal.IRON.ingots)
        .require(ItemTags.COALS)
        .requiresHeat(HeatCondition.HEATED)
        .output(CBCItems.STEEL_INGOT.get(), 2)),

	CONGEALED_NITRO = create(CreateBigCannons.resource("congealed_nitro"), b -> b.require(Items.BLAZE_POWDER)
		.require(Items.MAGMA_CREAM)
		.require(CBCItemTags.GUNPOWDER)
		.require(CBCItemTags.GUNPOWDER)
		.output(CBCItems.CONGEALED_NITRO.get(), 2)
		.duration(200)),

	CONGEALED_NITRO_NO_NETHER = create(CreateBigCannons.resource("congealed_nitro_no_nether"), b -> b.require(CBCItemTags.GUNCOTTON)
		.require(CBCItemTags.GELATINIZERS)
		.require(Fluids.WATER, 150 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.require(CBCItemTags.NITRO_ACIDIFIERS)
		.output(CBCItems.CONGEALED_NITRO.get())
		.duration(200)),

	GUNCOTTON = create(CreateBigCannons.resource("guncotton"), b -> b.require(CBCItemTags.CAN_BE_NITRATED)
		.require(CBCItemTags.GUNPOWDER)
		.require(Fluids.WATER, 200 * CBCDatagenCommon.FLUID_MULTIPLIER)
		.require(CBCItemTags.NITRO_ACIDIFIERS)
		.output(CBCItems.GUNCOTTON.get())
		.duration(300));

	public static ProcessingRecipeBuilder<ProcessingRecipe<?>> nethersteelAlloy(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, TagKey<Item> mixWith, int count, int yield) {
		builder.require(Items.NETHERITE_SCRAP);
		for (int i = 0; i < count; ++i) builder.require(mixWith);
		return builder.requiresHeat(HeatCondition.SUPERHEATED).output(CBCItems.NETHERSTEEL_INGOT.get(), yield).averageProcessingDuration();
	}

}
