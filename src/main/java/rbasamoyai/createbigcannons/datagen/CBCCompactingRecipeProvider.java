package rbasamoyai.createbigcannons.datagen;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.CommonMetal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class CBCCompactingRecipeProvider extends CompactingRecipeGen {

	public CBCCompactingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, CreateBigCannons.MOD_ID);
	}

	GeneratedRecipe

		PACKED_GUNPOWDER = create(CreateBigCannons.resource("packed_gunpowder"), b -> b.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.require(CBCTags.CBCItemTags.GUNPOWDER)
		.output(CBCItems.PACKED_GUNPOWDER)),

    FORGE_CAST_IRON_BLOCK = create(CreateBigCannons.resource("forge_cast_iron_block"), b -> b.require(fluidTag("molten_cast_iron"), 810)
        .output(CBCBlocks.CAST_IRON_BLOCK)),

	FORGE_CAST_IRON_INGOT = create(CreateBigCannons.resource("forge_cast_iron_ingot"), b -> b.require(fluidTag("molten_cast_iron"), 90)
		.output(CBCItems.CAST_IRON_INGOT)),

	FORGE_CAST_IRON_NUGGET = create(CreateBigCannons.resource("forge_cast_iron_nugget"), b -> b.require(fluidTag("molten_cast_iron"), 10)
		.output(CBCItems.CAST_IRON_NUGGET)),

    FORGE_BRONZE_BLOCK = create(CreateBigCannons.resource("forge_bronze_block"), b -> b.require(fluidTag("molten_bronze"), 810)
        .output(CBCBlocks.BRONZE_BLOCK)),

	FORGE_BRONZE_INGOT = create(CreateBigCannons.resource("forge_bronze_ingot"), b -> b.require(fluidTag("molten_bronze"), 90)
		.output(CBCItems.BRONZE_INGOT)),

    FORGE_BRONZE_NUGGET = create(CreateBigCannons.resource("forge_bronze_nugget"), b -> b.require(fluidTag("molten_bronze"), 10)
        .output(CBCItems.BRONZE_SCRAP)),

    FORGE_STEEL_BLOCK = create(CreateBigCannons.resource("forge_steel_block"), b -> b.require(fluidTag("molten_steel"), 810)
        .output(CBCBlocks.STEEL_BLOCK)),

	FORGE_STEEL_INGOT = create(CreateBigCannons.resource("forge_steel_ingot"), b -> b.require(fluidTag("molten_steel"), 90)
        .output(CBCItems.STEEL_INGOT)),

    FORGE_STEEL_NUGGET = create(CreateBigCannons.resource("forge_steel_nugget"), b -> b.require(fluidTag("molten_steel"), 10)
        .output(CBCItems.STEEL_SCRAP)),

    FORGE_NETHERSTEEL_BLOCK = create(CreateBigCannons.resource("forge_nethersteel_block"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 810)
        .output(CBCBlocks.NETHERSTEEL_BLOCK)),

	FORGE_NETHERSTEEL_INGOT = create(CreateBigCannons.resource("forge_nethersteel_ingot"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 90)
		.output(CBCItems.NETHERSTEEL_INGOT)),

	FORGE_NETHERSTEEL_NUGGET = create(CreateBigCannons.resource("forge_nethersteel_nugget"), b -> b.require(CBCFluids.MOLTEN_NETHERSTEEL.get(), 10)
		.output(CBCItems.NETHERSTEEL_NUGGET)),

	// The following are adapted from Create Deco
	IRON_TO_CAST_IRON_INGOT = create(CreateBigCannons.resource("iron_to_cast_iron_ingot"), b -> b.require(CommonMetal.IRON.ingots)
        .require(ItemTags.COALS)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCItems.CAST_IRON_INGOT)),

	IRON_TO_CAST_IRON_BLOCK = create(CreateBigCannons.resource("iron_to_cast_iron_block"), b -> b.require(CommonMetal.IRON.storageBlocks.items())
        .require(Tags.Items.STORAGE_BLOCKS_COAL)
		.requiresHeat(HeatCondition.HEATED)
		.output(CBCBlocks.CAST_IRON_BLOCK)),

	PACKED_GUNCOTTON = create(CreateBigCannons.resource("packed_guncotton"), b -> b.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.require(CBCTags.CBCItemTags.GUNCOTTON)
		.output(CBCItems.PACKED_GUNCOTTON));

    private static TagKey<Fluid> fluidTag(String path) {
        return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), ResourceLocation.fromNamespaceAndPath("c", path));
    }

}
