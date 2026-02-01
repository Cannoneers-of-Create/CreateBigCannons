package rbasamoyai.createbigcannons.datagen;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCCompactingRecipeProvider extends CompactingRecipeGen {

	public CBCCompactingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, CreateBigCannons.MOD_ID);
	}

	GeneratedRecipe

		PACKED_GUNPOWDER = create(CreateBigCannons.resource("packed_gunpowder"), b -> b.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.output(CBCItems.PACKED_GUNPOWDER.get())),

	FORGE_CAST_IRON_INGOT = create(CreateBigCannons.resource("forge_cast_iron_ingot"), b -> b.require(fluidTag("molten_cast_iron"), 90)
		.output(CBCItems.CAST_IRON_INGOT.get())),

	FORGE_CAST_IRON_NUGGET = create(CreateBigCannons.resource("forge_cast_iron_nugget"), b -> b.require(fluidTag("molten_cast_iron"), 10)
		.output(CBCItems.CAST_IRON_NUGGET.get())),

	FORGE_BRONZE_INGOT = create(CreateBigCannons.resource("forge_bronze_ingot"), b -> b
		.withCondition(new NotCondition(new TagEmptyCondition(CBCTags.CBCItemTags.INGOT_BRONZE)))
		.require(fluidTag("molten_bronze"), 90)
		.output(1, CBCUtils.location("alloyed", "bronze_ingot"), 1)),

	FORGE_STEEL_INGOT = create(CreateBigCannons.resource("forge_steel_ingot"), b -> b
        .withCondition(new NotCondition(new TagEmptyCondition(CBCTags.CBCItemTags.INGOT_STEEL)))
		.require(fluidTag("molten_steel"), 90)
		.output(1, CBCUtils.location("alloyed", "steel_ingot"), 1)),

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
		.output(CBCBlocks.CAST_IRON_BLOCK.get())),

	PACKED_GUNCOTTON = create(CreateBigCannons.resource("packed_guncotton"), b -> b.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.output(CBCItems.PACKED_GUNCOTTON.get()));

    private static TagKey<Fluid> fluidTag(String path) {
        return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), ResourceLocation.fromNamespaceAndPath("c", path));
    }

}
