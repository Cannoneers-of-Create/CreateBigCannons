package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.processing.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCFluids;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCCompactingRecipeProvider extends ProcessingRecipeGen {

	public CBCCompactingRecipeProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override protected IRecipeTypeInfo getRecipeType() { return AllRecipeTypes.COMPACTING; }
	
	GeneratedRecipe
	
	PACKED_GUNPOWDER = create(CreateBigCannons.resource("packed_gunpowder"), b -> b.require(Items.GUNPOWDER)
			.require(Items.GUNPOWDER)
			.require(Items.GUNPOWDER)
			.require(Items.GUNPOWDER)
			.require(Items.GUNPOWDER)
			.require(Items.GUNPOWDER)
			.output(CBCItems.PACKED_GUNPOWDER.get())),
	
	FORGE_CAST_IRON_INGOT = create(CreateBigCannons.resource("forge_cast_iron_ingot"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 90)
			.output(CBCItems.CAST_IRON_INGOT.get())),
	
	FORGE_CAST_IRON_NUGGET = create(CreateBigCannons.resource("forge_cast_iron_nugget"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 10)
			.output(CBCItems.CAST_IRON_NUGGET.get())),
	
	FORGE_BRONZE_INGOT = create(CreateBigCannons.resource("forge_bronze_ingot"), b -> b.whenModLoaded("alloyed")
			.require(AllTags.forgeFluidTag("molten_bronze"), 90)
			.output(1, new ResourceLocation("alloyed", "bronze_ingot"), 1)),

	FORGE_STEEL_INGOT = create(CreateBigCannons.resource("forge_steel_ingot"), b -> b.whenModLoaded("alloyed")
			.require(AllTags.forgeFluidTag("molten_steel"), 90)
			.output(1, new ResourceLocation("alloyed", "steel_ingot"), 1)),
	
	FORGE_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("forge_nethersteel_ingot"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 90)
			.output(CBCItems.NETHERSTEEL_INGOT.get())),
	
	FORGE_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("forge_nethersteel_nugget"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 10)
			.output(CBCItems.NETHERSTEEL_NUGGET.get())),
		
	// The following are reimplemented from Create Deco
	IRON_TO_CAST_IRON_INGOT = create(CreateBigCannons.resource("iron_to_cast_iron_ingot"), b -> b.require(Items.IRON_INGOT)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCItems.CAST_IRON_INGOT.get())),
	
	IRON_TO_CAST_IRON_BLOCK = create(CreateBigCannons.resource("iron_to_cast_iron_block"), b -> b.require(Items.IRON_BLOCK)
			.requiresHeat(HeatCondition.HEATED)
			.output(CBCBlocks.CAST_IRON_BLOCK.get()));

}
