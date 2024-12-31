package rbasamoyai.createbigcannons.datagen.recipes;

import static net.minecraft.data.recipes.RecipeProvider.getHasName;
import static net.minecraft.data.recipes.RecipeProvider.has;
import static net.minecraft.data.recipes.RecipeProvider.nineBlockStorageRecipesRecipesWithCustomUnpacking;
import static net.minecraft.data.recipes.RecipeProvider.nineBlockStorageRecipesWithCustomPacking;

import java.util.function.Consumer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;

public class CBCCraftingRecipeProvider {

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
			.define('I', CBCTags.CBCItemTags.INGOT_IRON).define('C', Items.CLOCK).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE)
			.pattern("I")
			.pattern("C")
			.pattern("R")
			.unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.PROXIMITY_FUZE.get(), 4)
			.define('A', Items.IRON_BARS).define('C', CBCTags.CBCItemTags.GEMS_QUARTZ).define('R', CBCTags.CBCItemTags.DUSTS_REDSTONE).define('I', CBCTags.CBCItemTags.INGOT_IRON)
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
			.define('I', CBCTags.CBCItemTags.INGOT_COPPER).define('W', CBCTags.CBCItemTags.DUSTS_REDSTONE)
			.pattern("WIW")
			.unlockedBy("has_redstone", has(CBCTags.CBCItemTags.DUSTS_REDSTONE))
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

		nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.MISC, CBCItems.GUNPOWDER_PINCH.get(), RecipeCategory.MISC, Items.GUNPOWDER, "gunpowder_from_pinches", "gunpowder");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.BIG_CARTRIDGE_SHEET.get(), 4)
			.define('S', CBCTags.CBCItemTags.SHEET_BRASS)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_brass_sheet", has(CBCTags.CBCItemTags.SHEET_BRASS))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.BIG_CARTRIDGE_SHEET.get())
			.define('S', CBCTags.CBCItemTags.INEXPENSIVE_BIG_CARTRIDGE_SHEET)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_inexpensive_big_cartridge_sheet", has(CBCTags.CBCItemTags.INEXPENSIVE_BIG_CARTRIDGE_SHEET))
			.save(cons, "big_cannon_sheet_inexpensive");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.AUTOCANNON_AMMO_CONTAINER.get())
			.define('I', CBCTags.CBCItemTags.SHEET_IRON).define('B', CBCTags.CBCItemTags.INGOT_BRASS)
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
			.define('B', CBCTags.CBCItemTags.NUGGET_COPPER)
			.pattern("B")
			.pattern("P")
			.pattern("C")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.CAST_IRON_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.CAST_IRON_BLOCK.get(), "cast_iron_ingot_from_block", "cast_iron_ingot");
		nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.CAST_IRON_NUGGET.get(), RecipeCategory.MISC, CBCItems.CAST_IRON_INGOT.get(), "cast_iron_ingot_from_nuggets", "cast_iron_ingot");

		nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, RecipeCategory.MISC, CBCItems.NETHERSTEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, CBCBlocks.NETHERSTEEL_BLOCK.get(), "nethersteel_ingot_from_block", "nethersteel_ingot");
		nineBlockStorageRecipesWithCustomPacking(cons, RecipeCategory.BUILDING_BLOCKS, CBCItems.NETHERSTEEL_NUGGET.get(), RecipeCategory.MISC, CBCItems.NETHERSTEEL_INGOT.get(), "nethersteel_ingot_from_nuggets", "nethersteel_ingot");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.SOLID_SHOT.get())
			.define('I', CBCTags.CBCItemTags.INGOT_IRON).define('i', CBCTags.CBCItemTags.NUGGET_IRON).define('S', ItemTags.WOODEN_SLABS)
			.pattern("iIi")
			.pattern("III")
			.pattern(" S ")
			.unlockedBy("has_iron_ingot", has(CBCTags.CBCItemTags.INGOT_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.AP_SHOT.get())
			.define('C', CBCTags.CBCItemTags.INGOT_CAST_IRON).define('I', CBCTags.CBCItemTags.INGOT_IRON).define('S', ItemTags.WOODEN_SLABS)
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
			.define('i', CBCTags.CBCItemTags.NUGGET_IRON).define('I', CBCTags.CBCItemTags.INGOT_IRON)
			.pattern("iii")
			.pattern("iIi")
			.pattern("iii")
			.unlockedBy("has_iron_nugget", has(CBCTags.CBCItemTags.NUGGET_IRON))
			.unlockedBy("has_iron_ingot", has(CBCTags.CBCItemTags.INGOT_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.BAG_OF_GRAPESHOT.get())
			.define('D', Items.STRING).define('W', ItemTags.WOOL).define('L', CBCItems.SHOT_BALLS.get()).define('S', ItemTags.WOODEN_SLABS)
			.pattern("DDD")
			.pattern("WLW")
			.pattern(" S ")
			.unlockedBy("has_iron_nugget", has(CBCTags.CBCItemTags.NUGGET_IRON))
			.unlockedBy("has_iron_ingot", has(CBCTags.CBCItemTags.INGOT_IRON))
			.save(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.SHRAPNEL_SHELL.get())
			.key('I', CBCTags.CBCItemTags.INGOT_IRON).key('L', CBCItems.SHOT_BALLS.get()).key('S', ItemTags.WOODEN_SLABS).key('P', CBCTags.CBCItemTags.GUNPOWDER)
			.patternLine(" I ")
			.patternLine("ILI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.HE_SHELL.get())
			.key('I', CBCTags.CBCItemTags.INGOT_IRON).key('T', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS)
			.key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("ITI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.AP_SHELL.get())
			.key('I', CBCTags.CBCItemTags.INGOT_IRON).key('C', CBCTags.CBCItemTags.INGOT_CAST_IRON)
			.key('T', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" C ")
			.patternLine("ICI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.FLUID_SHELL.get())
			.key('I', CBCTags.CBCItemTags.INGOT_IRON).key('P', AllBlocks.FLUID_PIPE.get()).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("IPI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.SMOKE_SHELL.get())
			.define('I', CBCTags.CBCItemTags.INGOT_IRON).define('P', CBCTags.CBCItemTags.SHEET_IRON).define('H', Items.HAY_BLOCK)
			.pattern("PHP")
			.pattern("PHP")
			.pattern("PIP")
			.unlockedBy(getHasName(Items.HAY_BLOCK), has(Items.HAY_BLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.DROP_MORTAR_SHELL.get())
			.define('X', CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS).define('I', CBCTags.CBCItemTags.SHEET_IRON)
			.define('S', AllBlocks.SHAFT.get()).define('C', CBCBlocks.POWDER_CHARGE.get())
			.pattern(" X ")
			.pattern("ISI")
			.pattern(" C ")
			.unlockedBy("has_high_explosives", has(CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.AP_AUTOCANNON_ROUND.get(), 4)
			.define('C', CBCTags.CBCItemTags.INGOT_CAST_IRON).define('I', CBCTags.CBCItemTags.INGOT_IRON)
			.pattern("C")
			.pattern("I")
			.unlockedBy("has_cast_iron", has(CBCTags.CBCItemTags.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.FLAK_AUTOCANNON_ROUND.get(), 2)
			.define('S', CBCItems.SHOT_BALLS.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('C', CBCTags.CBCItemTags.SHEET_IRON)
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
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('H', CBCTags.CBCItemTags.SHEET_IRON).define('h', CBCTags.CBCItemTags.NUGGET_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("hHh")
			.pattern(" C ")
			.pattern(" P ")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_DRILL.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('D', CBCTags.CBCItemTags.INGOT_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get()).define('p', AllBlocks.FLUID_PIPE.get())
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
			.define('I', CBCTags.CBCItemTags.SHEET_IRON).define('K', CBCTags.CBCItemTags.INGOT_IRON).define('G', CBCTags.CBCItemTags.GUNPOWDER)
			.pattern(" K ")
			.pattern("III")
			.pattern(" G ")
			.unlockedBy("has_gunpowder", has(CBCTags.CBCItemTags.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.get())
			.define('I', CBCTags.CBCItemTags.SHEET_IRON).define('G', CBCTags.CBCItemTags.GUNPOWDER)
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
			.save(cons, "wrought_iron_drop_mortar_end_mirrored");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCBlocks.CANNON_MOUNT.get())
			.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('I', CBCTags.CBCItemTags.SHEET_IRON)
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
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.CBCItemTags.GUNPOWDER).define('I', CBCTags.CBCItemTags.SHEET_IRON)
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
			.define('I', CBCTags.CBCItemTags.INGOT_STEEL).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_steel_ingot", has(CBCTags.CBCItemTags.INGOT_STEEL))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.STEEL_SCREW_LOCK.get())
			.define('I', CBCTags.CBCItemTags.INGOT_STEEL).define('S', AllBlocks.SHAFT.get())
			.pattern(" S ")
			.pattern("III")
			.unlockedBy("has_steel_ingot", has(CBCTags.CBCItemTags.INGOT_STEEL))
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
			.define('S', CBCTags.CBCItemTags.SHEET_IRON).define('L', ItemTags.LOGS)
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
			.define('V', AllTags.AllItemTags.VALVE_HANDLES.tag).define('I', CBCTags.CBCItemTags.SHEET_IRON)
			.define('B', CBCTags.CBCItemTags.SHEET_BRASS).define('Z', AllBlocks.BLAZE_BURNER.get())
			.pattern("VI ")
			.pattern("BZB")
			.pattern(" B ")
			.unlockedBy(getHasName(AllBlocks.BLAZE_BURNER.get()), has(AllBlocks.BLAZE_BURNER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.CANNON_WELDER.get())
			.define('V', AllTags.AllItemTags.VALVE_HANDLES.tag).define('I', CBCTags.CBCItemTags.SHEET_IRON)
			.define('B', CBCTags.CBCItemTags.SHEET_BRASS).define('Z', AllBlocks.BLAZE_BURNER.get())
			.pattern(" IV")
			.pattern("BZB")
			.pattern(" B ")
			.unlockedBy(getHasName(AllBlocks.BLAZE_BURNER.get()), has(AllBlocks.BLAZE_BURNER.get()))
			.save(cons, "cannon_welder_mirrored");

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CBCItems.GAS_MASK.get())
			.define('L', Items.LEATHER).define('G', CBCTags.CBCItemTags.GLASS).define('W', ItemTags.WOOL)
			.pattern(" L ")
			.pattern("LGL")
			.pattern(" W ")
			.unlockedBy("has_wool", has(ItemTags.WOOL))
			.save(cons);

		SpecialRecipeBuilder.special(CBCRecipeTypes.MUNITION_FUZING.getSerializer()).save(cons, "munition_fuzing");
		SpecialRecipeBuilder.special(CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer()).save(cons, "cartridge_assembly");
		SpecialRecipeBuilder.special(CBCRecipeTypes.BIG_CARTRIDGE_FILLING.getSerializer()).save(cons, "big_cartridge_filling");
		SpecialRecipeBuilder.special(CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getSerializer()).save(cons, "big_cartridge_filling_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getSerializer()).save(cons, "munition_fuzing_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer()).save(cons, "cartridge_assembly_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.TRACER_APPLICATION.getSerializer()).save(cons, "tracer_application");
		SpecialRecipeBuilder.special(CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getSerializer()).save(cons, "tracer_application_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER.getSerializer()).save(cons, "autocannon_ammo_container_filling_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.FUZE_REMOVAL.getSerializer()).save(cons, "fuze_removal");
		SpecialRecipeBuilder.special(CBCRecipeTypes.TRACER_REMOVAL.getSerializer()).save(cons, "tracer_removal");

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CBCItems.CONGEALED_NITRO.get()), RecipeCategory.MISC, CBCItems.HARDENED_NITRO.get(), 5, 200)
			.unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
			.save(cons);
	}

}
