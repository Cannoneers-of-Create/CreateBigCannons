package rbasamoyai.createbigcannons.datagen.recipes;

import java.util.function.Consumer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;

public abstract class CBCCraftingRecipeProvider extends RecipeProvider {

    private CBCCraftingRecipeProvider(PackOutput output) { super(output); }

    public static void register() {
		CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.RECIPE, CBCCraftingRecipeProvider::buildCraftingRecipes);
	}

	public static void buildCraftingRecipes(Consumer<FinishedRecipe> cons) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.IMPACT_FUZE.get(), 4)
			.define('T', CBCTags.CBCItemTags.IMPACT_FUZE_HEAD).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE)
			.pattern("T")
			.pattern("R")
			.unlockedBy("has_impact_fuze_head", has(CBCTags.CBCItemTags.IMPACT_FUZE_HEAD))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.TIMED_FUZE.get(), 4)
			.define('I', CommonMetal.IRON.ingots).define('C', Items.CLOCK).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE)
			.pattern("I")
			.pattern("C")
			.pattern("R")
			.unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.PROXIMITY_FUZE.get(), 4)
			.define('A', Items.IRON_BARS).define('C', CBCTags.CBCItemTags.GEMS_QUARTZ).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE).define('I', CommonMetal.IRON.ingots)
			.pattern(" A ")
			.pattern("RCR")
			.pattern(" I ")
			.unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
			.unlockedBy("has_quartz", has(CBCTags.CBCItemTags.GEMS_QUARTZ))
			.save(cons);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CBCItems.DELAYED_IMPACT_FUZE.get())
			.requires(CBCItems.TIMED_FUZE.get()).requires(CBCItems.IMPACT_FUZE.get())
			.unlockedBy(getHasName(CBCItems.TIMED_FUZE.get()), has(CBCItems.TIMED_FUZE.get()))
			.unlockedBy(getHasName(CBCItems.IMPACT_FUZE.get()), has(CBCItems.IMPACT_FUZE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.WIRED_FUZE.get(), 4)
			.define('I', CommonMetal.COPPER.ingots).define('W', CBCTags.CBCItemTags.DUSTS_REDSTONE)
			.pattern("WIW")
			.unlockedBy("has_redstone", has(CBCTags.CBCItemTags.DUSTS_REDSTONE))
			.save(cons);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.INERTIA_FUZE.get(), 2)
            .define('T', CBCTags.CBCItemTags.IMPACT_FUZE_HEAD).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE).define('S', CBCItems.RECOIL_SPRING)
            .pattern("T")
            .pattern("S")
            .pattern("R")
            .unlockedBy("has_impact_fuze_head", has(CBCTags.CBCItemTags.IMPACT_FUZE_HEAD))
            .unlockedBy(getHasName(CBCItems.RECOIL_SPRING.get()), has(CBCItems.RECOIL_SPRING.get()))
            .save(cons);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CBCItems.DELAYED_INERTIA_FUZE.get())
            .requires(CBCItems.TIMED_FUZE.get()).requires(CBCItems.INERTIA_FUZE.get())
            .unlockedBy(getHasName(CBCItems.TIMED_FUZE.get()), has(CBCItems.TIMED_FUZE.get()))
            .unlockedBy(getHasName(CBCItems.INERTIA_FUZE.get()), has(CBCItems.INERTIA_FUZE.get()))
            .save(cons);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CBCItems.TRACER_TIP.get(), 4)
			.requires(CBCTags.CBCItemTags.DUST_GLOWSTONE).requires(Items.BLAZE_POWDER)
			.unlockedBy("has_glowstone", has(CBCTags.CBCItemTags.DUST_GLOWSTONE))
			.unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.EMPTY_POWDER_CHARGE.get())
			.define('W', ItemTags.WOOL).define('S', Items.STRING)
			.pattern("S")
			.pattern("W")
			.pattern("S")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CBCBlocks.POWDER_CHARGE.get())
			.requires(CBCItems.PACKED_GUNPOWDER.get())
			.requires(CBCItems.EMPTY_POWDER_CHARGE.get())
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		cbc$nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.MISC, CBCItems.GUNPOWDER_PINCH.get(), RecipeCategory.MISC, Items.GUNPOWDER, "gunpowder_from_pinches", "gunpowder");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.BIG_CARTRIDGE_SHEET.get(), 4)
			.define('S', CommonMetal.BRASS.plates)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_brass_sheet", has(CommonMetal.BRASS.plates))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.BIG_CARTRIDGE_SHEET.get())
			.define('S', CBCTags.CBCItemTags.INEXPENSIVE_BIG_CARTRIDGE_SHEET)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_inexpensive_big_cartridge_sheet", has(CBCTags.CBCItemTags.INEXPENSIVE_BIG_CARTRIDGE_SHEET))
			.save(cons, CreateBigCannons.resource("big_cannon_sheet_inexpensive"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.AUTOCANNON_AMMO_CONTAINER.get())
			.define('I', CommonMetal.IRON.plates).define('B', CommonMetal.BRASS.ingots)
			.pattern(" B ")
			.pattern("I I")
			.pattern("III")
			.unlockedBy(getHasName(CBCItems.AUTOCANNON_CARTRIDGE.get()), has(CBCItems.AUTOCANNON_CARTRIDGE.get()))
			.unlockedBy(getHasName(CBCItems.MACHINE_GUN_ROUND.get()), has(CBCItems.MACHINE_GUN_ROUND.get()))
			.save(cons);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CBCBlocks.CASTING_SAND.get())
			.requires(Items.SAND, 2)
			.requires(Items.DIRT)
			.requires(Items.CLAY_BALL)
			.unlockedBy(getHasName(Items.SAND), has(Items.SAND))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.MACHINE_GUN_ROUND.get())
			.define('C', CBCItems.EMPTY_MACHINE_GUN_ROUND.get()).define('P', CBCTags.CBCItemTags.GUNPOWDER)
			.define('B', CommonMetal.COPPER.nuggets)
			.pattern("B")
			.pattern("P")
			.pattern("C")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		cbc$nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.CAST_IRON_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.CAST_IRON_BLOCK.get(), "cast_iron_ingot_from_block", "cast_iron_ingot");
		cbc$nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.CAST_IRON_NUGGET.get(), RecipeCategory.MISC, CBCItems.CAST_IRON_INGOT.get(), "cast_iron_ingot_from_nuggets", "cast_iron_ingot");

        cbc$nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.BRONZE_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.BRONZE_BLOCK.get(), "bronze_ingot_from_block", "bronze_ingot");
        cbc$nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.BRONZE_SCRAP.get(), RecipeCategory.MISC, CBCItems.BRONZE_INGOT.get(), "bronze_ingot_from_nuggets", "bronze_ingot");

        cbc$nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.STEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.STEEL_BLOCK.get(), "steel_ingot_from_block", "steel_ingot");
        cbc$nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.STEEL_SCRAP.get(), RecipeCategory.MISC, CBCItems.STEEL_INGOT.get(), "steel_ingot_from_nuggets", "steel_ingot");

		cbc$nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.NETHERSTEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.NETHERSTEEL_BLOCK.get(), "nethersteel_ingot_from_block", "nethersteel_ingot");
		cbc$nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.NETHERSTEEL_NUGGET.get(), RecipeCategory.MISC, CBCItems.NETHERSTEEL_INGOT.get(), "nethersteel_ingot_from_nuggets", "nethersteel_ingot");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.SOLID_SHOT.get())
			.define('I', CommonMetal.IRON.ingots).define('i', CommonMetal.IRON.nuggets).define('S', ItemTags.WOODEN_SLABS)
			.pattern("iIi")
			.pattern("III")
			.pattern(" S ")
			.unlockedBy("has_iron_ingot", has(CommonMetal.IRON.ingots))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.AP_SHOT.get())
			.define('C', CBCTags.CBCItemTags.INGOT_CAST_IRON).define('I', CommonMetal.IRON.ingots).define('S', ItemTags.WOODEN_SLABS)
			.pattern(" C ")
			.pattern("III")
			.pattern(" S ")
			.unlockedBy("has_cast_iron_ingot", has(CBCTags.CBCItemTags.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.MORTAR_STONE.get())
			.define('S', CBCTags.CBCItemTags.STONE).define('s', ItemTags.WOODEN_SLABS)
			.pattern(" S ")
			.pattern("SSS")
			.pattern(" s ")
			.unlockedBy("has_stone", has(CBCTags.CBCItemTags.STONE))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.SHOT_BALLS.get(), 2)
			.define('i', CommonMetal.IRON.nuggets).define('I', CommonMetal.IRON.ingots)
			.pattern("iii")
			.pattern("iIi")
			.pattern("iii")
			.unlockedBy("has_iron_nugget", has(CommonMetal.IRON.nuggets))
			.unlockedBy("has_iron_ingot", has(CommonMetal.IRON.ingots))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.BAG_OF_GRAPESHOT.get())
			.define('D', Items.STRING).define('W', ItemTags.WOOL).define('L', CBCItems.SHOT_BALLS.get()).define('S', ItemTags.WOODEN_SLABS)
			.pattern("DDD")
			.pattern("WLW")
			.pattern(" S ")
			.unlockedBy("has_iron_nugget", has(CommonMetal.IRON.nuggets))
			.unlockedBy("has_iron_ingot", has(CommonMetal.IRON.ingots))
			.save(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.SHRAPNEL_SHELL.get())
			.key('I', CommonMetal.IRON.ingots).key('L', CBCItems.SHOT_BALLS.get()).key('S', ItemTags.WOODEN_SLABS).key('P', CBCTags.CBCItemTags.GUNPOWDER)
			.patternLine(" I ")
			.patternLine("ILI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.HE_SHELL.get())
			.key('I', CommonMetal.IRON.ingots).key('T', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS)
			.key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("ITI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.AP_SHELL.get())
			.key('I', CommonMetal.IRON.ingots).key('C', CBCTags.CBCItemTags.INGOT_CAST_IRON)
			.key('T', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" C ")
			.patternLine("ICI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.FLUID_SHELL.get())
			.key('I', CommonMetal.IRON.ingots).key('P', AllBlocks.FLUID_PIPE.get()).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("IPI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.SMOKE_SHELL.get())
			.define('I', CommonMetal.IRON.ingots).define('P', CommonMetal.IRON.plates).define('H', Items.HAY_BLOCK)
			.pattern("PHP")
			.pattern("PHP")
			.pattern("PIP")
			.unlockedBy(getHasName(Items.HAY_BLOCK), has(Items.HAY_BLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.DROP_MORTAR_SHELL.get())
			.define('X', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS).define('I', CommonMetal.IRON.plates)
			.define('S', AllBlocks.SHAFT.get()).define('C', CBCBlocks.POWDER_CHARGE.get())
			.pattern(" X ")
			.pattern("ISI")
			.pattern(" C ")
			.unlockedBy("has_high_explosives", has(CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.AP_AUTOCANNON_ROUND.get(), 4)
			.define('C', CBCTags.CBCItemTags.INGOT_CAST_IRON).define('I', CommonMetal.IRON.ingots)
			.pattern("C")
			.pattern("I")
			.unlockedBy("has_cast_iron", has(CBCTags.CBCItemTags.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.FLAK_AUTOCANNON_ROUND.get(), 2)
			.define('S', CBCItems.SHOT_BALLS.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('C', CommonMetal.IRON.plates)
			.pattern("S")
			.pattern("G")
			.pattern("C")
			.unlockedBy(getHasName(CBCItems.SHOT_BALLS.get()), has(CBCItems.SHOT_BALLS.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_LOADER.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("G")
			.pattern("C")
			.pattern("P")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.RAM_HEAD.get())
			.define('H', ItemTags.PLANKS).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("H")
			.pattern("P")
			.unlockedBy(getHasName(CBCBlocks.CANNON_LOADER.get()), has(CBCBlocks.CANNON_LOADER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WORM_HEAD.get())
			.define('H', Items.IRON_BARS).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("HHH")
			.pattern(" P ")
			.unlockedBy(getHasName(CBCBlocks.CANNON_LOADER.get()), has(CBCBlocks.CANNON_LOADER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_BUILDER.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('H', CommonMetal.IRON.plates).define('h', CommonMetal.IRON.nuggets).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("hHh")
			.pattern(" C ")
			.pattern(" P ")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_DRILL.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('D', CommonMetal.IRON.ingots).define('P', AllBlocks.PISTON_EXTENSION_POLE.get()).define('p', AllBlocks.FLUID_PIPE.get())
			.pattern(" D ")
			.pattern("pCp")
			.pattern(" P ")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.BASIN_FOUNDRY_LID.get())
			.define('A', AllItems.ANDESITE_ALLOY.get())
			.pattern(" A ")
			.pattern("AAA")
			.unlockedBy(getHasName(AllBlocks.BASIN.get()), has(AllBlocks.BASIN.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.QUICKFIRING_MECHANISM.get())
			.define('L', Items.LEVER).define('c', AllBlocks.COGWHEEL.get()).define('C', AllBlocks.LARGE_COGWHEEL.get())
			.pattern(" L")
			.pattern("cC")
			.unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.LOG_CANNON_END.get())
			.define('L', ItemTags.LOGS).define('K', ItemTags.WOODEN_BUTTONS).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" K ")
			.pattern("LLL")
			.pattern(" G ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.LOG_CANNON_CHAMBER.get())
			.define('L', ItemTags.LOGS).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" L ")
			.pattern("LGL")
			.pattern(" L ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WROUGHT_IRON_CANNON_END.get())
			.define('I', CommonMetal.IRON.plates).define('K', CommonMetal.IRON.ingots).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" K ")
			.pattern("III")
			.pattern(" G ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.get())
			.define('I', CommonMetal.IRON.plates).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" I ")
			.pattern("IGI")
			.pattern(" I ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WROUGHT_IRON_DROP_MORTAR_END.get())
			.define('B', Items.IRON_BARS).define('E', CBCBlocks.WROUGHT_IRON_CANNON_END.get())
			.pattern("E")
			.pattern("B")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WROUGHT_IRON_DROP_MORTAR_END.get())
			.define('B', Items.IRON_BARS).define('E', CBCBlocks.WROUGHT_IRON_CANNON_END.get())
			.pattern("B")
			.pattern("E")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons, CreateBigCannons.resource("wrought_iron_drop_mortar_end_mirrored"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_MOUNT.get())
			.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('I', CommonMetal.IRON.plates)
			.pattern("ISI")
			.pattern("SCS")
			.pattern("GSG")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_MOUNT_EXTENSION.get())
			.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" S ")
			.pattern("GCG")
			.pattern(" S ")
			.unlockedBy(getHasName(CBCBlocks.CANNON_MOUNT.get()), has(CBCBlocks.CANNON_MOUNT.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.FIXED_CANNON_MOUNT.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('I', CommonMetal.IRON.plates)
			.pattern(" I ")
			.pattern("GCG")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.CAST_IRON_SLIDING_BREECHBLOCK.get())
			.define('I', CBCTags.CBCItemTags.INGOT_CAST_IRON).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_cast_iron_ingot", has(CBCTags.CBCItemTags.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.BRONZE_SLIDING_BREECHBLOCK.get())
			.define('I', CBCTags.CBCItemTags.INGOT_BRONZE).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_bronze_ingot", has(CBCTags.CBCItemTags.INGOT_BRONZE))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.STEEL_SLIDING_BREECHBLOCK.get())
			.define('I', CommonMetal.STEEL.ingots).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_steel_ingot", has(CommonMetal.STEEL.ingots))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.STEEL_SCREW_LOCK.get())
			.define('I', CommonMetal.STEEL.ingots).define('S', AllBlocks.SHAFT.get())
			.pattern(" S ")
			.pattern("III")
			.unlockedBy("has_steel_ingot", has(CommonMetal.STEEL.ingots))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.NETHERSTEEL_SCREW_LOCK.get())
			.define('I', CBCItems.NETHERSTEEL_INGOT.get()).define('S', AllBlocks.SHAFT.get())
			.pattern(" S ")
			.pattern("III")
			.unlockedBy(getHasName(CBCItems.NETHERSTEEL_INGOT.get()), has(CBCItems.NETHERSTEEL_INGOT.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.RAM_ROD.get())
			.define('H', CBCBlocks.RAM_HEAD.get()).define('S', Items.STICK)
			.pattern("H")
			.pattern("S")
			.pattern("S")
			.unlockedBy(getHasName(CBCBlocks.RAM_HEAD.get()), has(CBCBlocks.RAM_HEAD.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.WORM.get())
			.define('H', CBCBlocks.WORM_HEAD.get()).define('S', Items.STICK)
			.pattern("H")
			.pattern("S")
			.pattern("S")
			.unlockedBy(getHasName(CBCBlocks.WORM_HEAD.get()), has(CBCBlocks.WORM_HEAD.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.PAIR_OF_CANNON_WHEELS.get(), 2)
			.define('S', CommonMetal.IRON.plates).define('L', ItemTags.LOGS)
			.pattern(" S ")
			.pattern("SLS")
			.pattern(" S ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_CARRIAGE.get())
			.define('P', ItemTags.PLANKS).define('S', AllBlocks.SHAFT.get()).define('W', CBCItems.PAIR_OF_CANNON_WHEELS.get())
			.pattern(" SP")
			.pattern("PPP")
			.pattern("W W")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.CANNON_WELDER.get())
			.define('V', AllTags.AllItemTags.VALVE_HANDLES.tag).define('I', CommonMetal.IRON.plates)
			.define('B', CommonMetal.BRASS.plates).define('Z', AllBlocks.BLAZE_BURNER.get())
			.pattern("VI ")
			.pattern("BZB")
			.pattern(" B ")
			.unlockedBy(getHasName(AllBlocks.BLAZE_BURNER.get()), has(AllBlocks.BLAZE_BURNER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.CANNON_WELDER.get())
			.define('V', AllTags.AllItemTags.VALVE_HANDLES.tag).define('I', CommonMetal.IRON.plates)
			.define('B', CommonMetal.BRASS.plates).define('Z', AllBlocks.BLAZE_BURNER.get())
			.pattern(" IV")
			.pattern("BZB")
			.pattern(" B ")
			.unlockedBy(getHasName(AllBlocks.BLAZE_BURNER.get()), has(AllBlocks.BLAZE_BURNER.get()))
			.save(cons, CreateBigCannons.resource("cannon_welder_mirrored"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.GAS_MASK.get())
			.define('L', Items.LEATHER).define('G', CBCTags.CBCItemTags.GLASS).define('W', ItemTags.WOOL)
			.pattern(" L ")
			.pattern("LGL")
			.pattern(" W ")
			.unlockedBy("has_wool", has(ItemTags.WOOL))
			.save(cons);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CBCItems.BLOCK_ARMOR_INSPECTION_TOOL)
            .define('M', AllItems.PRECISION_MECHANISM).define('S', CBCItems.RECOIL_SPRING).define('P', Items.IRON_BARS)
            .pattern("M")
            .pattern("S")
            .pattern("P")
            .unlockedBy(getHasName(CBCItems.RECOIL_SPRING), has(CBCItems.RECOIL_SPRING))
            .unlockedBy(getHasName(AllItems.PRECISION_MECHANISM), has(AllItems.PRECISION_MECHANISM))
            .save(cons);

		specialRecipe(CBCRecipeTypes.MUNITION_FUZING.getSerializer(), cons, "munition_fuzing");
		specialRecipe(CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer(), cons, "cartridge_assembly");
		specialRecipe(CBCRecipeTypes.BIG_CARTRIDGE_FILLING.getSerializer(), cons, "big_cartridge_filling");
		specialRecipe(CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getSerializer(), cons, "big_cartridge_filling_deployer");
		specialRecipe(CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getSerializer(), cons, "munition_fuzing_deployer");
		specialRecipe(CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer(), cons, "cartridge_assembly_deployer");
		specialRecipe(CBCRecipeTypes.TRACER_APPLICATION.getSerializer(), cons, "tracer_application");
		specialRecipe(CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getSerializer(), cons, "tracer_application_deployer");
		specialRecipe(CBCRecipeTypes.AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER.getSerializer(), cons, "autocannon_ammo_container_filling_deployer");
		specialRecipe(CBCRecipeTypes.FUZE_REMOVAL.getSerializer(), cons, "fuze_removal");
		specialRecipe(CBCRecipeTypes.TRACER_REMOVAL.getSerializer(), cons, "tracer_removal");

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CBCItems.CONGEALED_NITRO.get()), RecipeCategory.MISC, CBCItems.HARDENED_NITRO.get(), 5, 200)
			.unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
			.save(cons);
	}

    private static void specialRecipe(RecipeSerializer<? extends CraftingRecipe> ser, Consumer<FinishedRecipe> cons, String id) {
        SpecialRecipeBuilder.special(ser).save(cons, CreateBigCannons.resource(id).toString());
    }

    private static void cbc$nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> finishedRecipeConsumer,
                                                                     RecipeCategory unpackedCategory, ItemLike unpacked,
                                                                     RecipeCategory packedCategory, ItemLike packed,
                                                                     String packedName, String packedGroup) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpackedCategory, unpacked, packedCategory, packed,
            CreateBigCannons.resource(packedName).toString(), CreateBigCannons.resource(packedGroup).toString(),
            cbc$getSimpleRecipeName(unpacked), null);
    }

    private static void cbc$nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> finishedRecipeConsumer,
                                                                              RecipeCategory unpackedCategory, ItemLike unpacked,
                                                                              RecipeCategory packedCategory, ItemLike packed,
                                                                              String unpackedName, String unpackedGroup) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpackedCategory, unpacked, packedCategory, packed,
            cbc$getSimpleRecipeName(packed), null, CreateBigCannons.resource(unpackedName).toString(),
            CreateBigCannons.resource(unpackedGroup).toString());
    }

    private static String cbc$getSimpleRecipeName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).toString();
    }

}
