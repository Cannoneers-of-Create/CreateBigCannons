package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCMixingRecipeProvider extends ProcessingRecipeGen {

	public CBCMixingRecipeProvider(DataGenerator gen) {
		super(gen);
	}
	
	@Override protected IRecipeTypeInfo getRecipeType() { return AllRecipeTypes.MIXING; }
	
	GeneratedRecipe
	
	ALLOY_NETHERSTEEL_CAST_IRON = create(CreateBigCannons.resource("alloy_nethersteel_cast_iron"), b -> nethersteelAlloy(b, CBCTags.ItemCBC.INGOT_CAST_IRON, 8, 8)),
	
	ALLOY_NETHERSTEEL_STEEL = create(CreateBigCannons.resource("alloy_nethersteel_steel"), b -> nethersteelAlloy(b, CBCTags.ItemCBC.INGOT_STEEL, 4, 8));
	
	public static ProcessingRecipeBuilder<ProcessingRecipe<?>> nethersteelAlloy(ProcessingRecipeBuilder<ProcessingRecipe<?>> builder, TagKey<Item> mixWith, int count, int yield) {
		builder.require(Items.NETHERITE_SCRAP);
		for (int i = 0; i < count; ++i) builder.require(mixWith);
		return builder.requiresHeat(HeatCondition.SUPERHEATED).output(CBCItems.NETHERSTEEL_INGOT.get(), 8);
	}
	
}
