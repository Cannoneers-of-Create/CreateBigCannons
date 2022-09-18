package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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
	
	FORGE_CAST_IRON_INGOT = create(CreateBigCannons.resource("forge_cast_iron_ingot"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 144)
			.output(CBCItems.CAST_IRON_INGOT.get())),
	
	FORGE_CAST_IRON_NUGGET = create(CreateBigCannons.resource("forge_cast_iron_nugget"), b -> b.require(AllTags.forgeFluidTag("molten_cast_iron"), 16)
			.output(CBCItems.CAST_IRON_NUGGET.get())),
	
	FORGE_BRONZE_INGOT = create(CreateBigCannons.resource("forge_bronze_ingot"), b -> b.whenModLoaded("alloyed")
			.require(AllTags.forgeFluidTag("molten_bronze"), 144)
			.output(1, new ResourceLocation("alloyed", "bronze_ingot"), 1)),

	FORGE_STEEL_INGOT = create(CreateBigCannons.resource("forge_steel_ingot"), b -> b.whenModLoaded("alloyed")
			.require(AllTags.forgeFluidTag("molten_steel"), 144)
			.output(1, new ResourceLocation("alloyed", "steel_ingot"), 1));
		
}
