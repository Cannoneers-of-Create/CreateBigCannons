package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCMixingRecipeProvider extends ProcessingRecipeGen {

	public CBCMixingRecipeProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MIXING;
	}

	GeneratedRecipe

		ALLOY_NETHERSTEEL_CAST_IRON = create(CreateBigCannons.resource("alloy_nethersteel_cast_iron"), b -> nethersteelAlloy(b, CBCItemTags.INGOT_CAST_IRON, 8, 8)),

	ALLOY_NETHERSTEEL_STEEL = create(CreateBigCannons.resource("alloy_nethersteel_steel"), b -> nethersteelAlloy(b, CBCItemTags.INGOT_STEEL, 4, 8)),

	CONGEALED_NITRO = create(CreateBigCannons.resource("congealed_nitro"), b -> b.require(Items.BLAZE_POWDER)
		.require(Items.MAGMA_CREAM)
		.require(CBCItemTags.GUNPOWDER)
		.require(CBCItemTags.GUNPOWDER)
		.output(CBCItems.CONGEALED_NITRO.get(), 2));

	public static ProcessingRecipeBuilder<ProcessingRecipe<?>> nethersteelAlloy(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, TagKey<Item> mixWith, int count, int yield) {
		builder.require(Items.NETHERITE_SCRAP);
		for (int i = 0; i < count; ++i) builder.require(mixWith);
		return builder.requiresHeat(HeatCondition.SUPERHEATED).output(CBCItems.NETHERSTEEL_INGOT.get(), yield);
	}

}
