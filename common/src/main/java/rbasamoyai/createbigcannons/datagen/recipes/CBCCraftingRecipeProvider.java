package rbasamoyai.createbigcannons.datagen.recipes;

import static net.minecraft.data.recipes.RecipeProvider.getHasName;
import static net.minecraft.data.recipes.RecipeProvider.has;
import static net.minecraft.data.recipes.RecipeProvider.nineBlockStorageRecipesRecipesWithCustomUnpacking;
import static net.minecraft.data.recipes.RecipeProvider.nineBlockStorageRecipesWithCustomPacking;

import java.util.function.Consumer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import com.tterrag.registrate.providers.ProviderType;

import net.minecraft.data.recipes.FinishedRecipe;
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
		ShapedRecipeBuilder.shaped(CBCItems.IMPACT_FUZE.get(), 4)
			.define('T', CBCTags.ItemCBC.IMPACT_FUZE_HEAD).define('R', CBCTags.ItemCBC.DUSTS_REDSTONE)
			.pattern("T")
			.pattern("R")
			.unlockedBy("has_impact_fuze_head", has(CBCTags.ItemCBC.IMPACT_FUZE_HEAD))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.TIMED_FUZE.get(), 4)
			.define('I', CBCTags.ItemCBC.INGOT_IRON).define('C', Items.CLOCK).define('R', CBCTags.ItemCBC.DUSTS_REDSTONE)
			.pattern("I")
			.pattern("C")
			.pattern("R")
			.unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.PROXIMITY_FUZE.get(), 4)
			.define('A', Items.IRON_BARS).define('C', CBCTags.ItemCBC.GEMS_QUARTZ).define('R', CBCTags.ItemCBC.DUSTS_REDSTONE).define('I', CBCTags.ItemCBC.INGOT_IRON)
			.pattern(" A ")
			.pattern("RCR")
			.pattern(" I ")
			.unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
			.unlockedBy("has_quartz", has(CBCTags.ItemCBC.GEMS_QUARTZ))
			.save(cons);

		ShapelessRecipeBuilder.shapeless(CBCItems.DELAYED_IMPACT_FUZE.get())
			.requires(CBCItems.TIMED_FUZE.get()).requires(CBCItems.IMPACT_FUZE.get())
			.unlockedBy(getHasName(CBCItems.TIMED_FUZE.get()), has(CBCItems.TIMED_FUZE.get()))
			.unlockedBy(getHasName(CBCItems.IMPACT_FUZE.get()), has(CBCItems.IMPACT_FUZE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.EMPTY_POWDER_CHARGE.get())
			.define('W', ItemTags.WOOL).define('S', Items.STRING)
			.pattern("S")
			.pattern("W")
			.pattern("S")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapelessRecipeBuilder.shapeless(CBCBlocks.POWDER_CHARGE.get())
			.requires(CBCItems.PACKED_GUNPOWDER.get())
			.requires(CBCItems.EMPTY_POWDER_CHARGE.get())
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.BIG_CANNON_SHEET.get(), 2)
			.define('S', CBCTags.ItemCBC.SHEET_BRASS)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_brass_sheet", has(CBCTags.ItemCBC.SHEET_BRASS))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.BIG_CANNON_SHEET.get(), 1)
			.define('S', CBCTags.ItemCBC.INEXPENSIVE_BIG_CARTRIDGE_SHEET)
			.pattern("SS")
			.pattern("SS")
			.unlockedBy("has_inexpensive_big_cartridge_sheet", has(CBCTags.ItemCBC.INEXPENSIVE_BIG_CARTRIDGE_SHEET))
			.save(cons, "big_cannon_sheet_inexpensive");

		ShapelessRecipeBuilder.shapeless(CBCBlocks.CASTING_SAND.get())
			.requires(Items.SAND, 2)
			.requires(Items.DIRT)
			.requires(Items.CLAY_BALL)
			.unlockedBy(getHasName(Items.SAND), has(Items.SAND))
			.save(cons);

		nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, CBCItems.CAST_IRON_INGOT.get(), CBCBlocks.CAST_IRON_BLOCK.get(), "cast_iron_ingot_from_block", "cast_iron_ingot");
		nineBlockStorageRecipesWithCustomPacking(cons, CBCItems.CAST_IRON_NUGGET.get(), CBCItems.CAST_IRON_INGOT.get(), "cast_iron_ingot_from_nuggets", "cast_iron_ingot");

		nineBlockStorageRecipesRecipesWithCustomUnpacking(cons, CBCItems.NETHERSTEEL_INGOT.get(), CBCBlocks.NETHERSTEEL_BLOCK.get(), "nethersteel_ingot_from_block", "nethersteel_ingot");
		nineBlockStorageRecipesWithCustomPacking(cons, CBCItems.NETHERSTEEL_NUGGET.get(), CBCItems.NETHERSTEEL_INGOT.get(), "nethersteel_ingot_from_nuggets", "nethersteel_ingot");

		ShapedRecipeBuilder.shaped(CBCBlocks.SOLID_SHOT.get())
			.define('I', CBCTags.ItemCBC.INGOT_IRON).define('i', CBCTags.ItemCBC.NUGGET_IRON).define('S', ItemTags.WOODEN_SLABS)
			.pattern("iIi")
			.pattern("III")
			.pattern(" S ")
			.unlockedBy("has_iron_ingot", has(CBCTags.ItemCBC.INGOT_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.AP_SHOT.get())
			.define('C', CBCTags.ItemCBC.INGOT_CAST_IRON).define('I', CBCTags.ItemCBC.INGOT_IRON).define('S', ItemTags.WOODEN_SLABS)
			.pattern(" C ")
			.pattern("III")
			.pattern(" S ")
			.unlockedBy("has_cast_iron_ingot", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.MORTAR_STONE.get())
			.define('S', CBCTags.ItemCBC.STONE).define('s', ItemTags.WOODEN_SLABS)
			.pattern(" S ")
			.pattern("SSS")
			.pattern(" s ")
			.unlockedBy("has_stone", has(CBCTags.ItemCBC.STONE))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.SHOT_BALLS.get(), 2)
			.define('i', CBCTags.ItemCBC.NUGGET_IRON).define('I', CBCTags.ItemCBC.INGOT_IRON)
			.pattern("iii")
			.pattern("iIi")
			.pattern("iii")
			.unlockedBy("has_iron_nugget", has(CBCTags.ItemCBC.NUGGET_IRON))
			.unlockedBy("has_iron_ingot", has(CBCTags.ItemCBC.INGOT_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.BAG_OF_GRAPESHOT.get())
			.define('D', Items.STRING).define('W', ItemTags.WOOL).define('L', CBCItems.SHOT_BALLS.get()).define('S', ItemTags.WOODEN_SLABS)
			.pattern("DDD")
			.pattern("WLW")
			.pattern(" S ")
			.unlockedBy("has_iron_nugget", has(CBCTags.ItemCBC.NUGGET_IRON))
			.unlockedBy("has_iron_ingot", has(CBCTags.ItemCBC.INGOT_IRON))
			.save(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.SHRAPNEL_SHELL.get())
			.key('I', CBCTags.ItemCBC.INGOT_IRON).key('L', CBCItems.SHOT_BALLS.get()).key('S', ItemTags.WOODEN_SLABS).key('P', CBCTags.ItemCBC.GUNPOWDER)
			.patternLine(" I ")
			.patternLine("ILI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.HE_SHELL.get())
			.key('I', CBCTags.ItemCBC.INGOT_IRON).key('T', Items.TNT).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("ITI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.AP_SHELL.get())
			.key('I', CBCTags.ItemCBC.INGOT_IRON).key('C', CBCTags.ItemCBC.INGOT_CAST_IRON).key('T', Items.TNT).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" C ")
			.patternLine("ICI")
			.patternLine("ITI")
			.patternLine(" S ")
			.build(cons);

		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.FLUID_SHELL.get())
			.key('I', CBCTags.ItemCBC.INGOT_IRON).key('P', AllBlocks.FLUID_PIPE.get()).key('S', ItemTags.WOODEN_SLABS)
			.patternLine(" I ")
			.patternLine("IPI")
			.patternLine("IPI")
			.patternLine(" S ")
			.build(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.SMOKE_SHELL.get())
			.define('I', CBCTags.ItemCBC.INGOT_IRON).define('P', CBCTags.ItemCBC.SHEET_IRON).define('H', Items.HAY_BLOCK)
			.pattern("PHP")
			.pattern("PHP")
			.pattern("PIP")
			.unlockedBy(getHasName(Items.HAY_BLOCK), has(Items.HAY_BLOCK))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.AP_AUTOCANNON_ROUND.get(), 4)
			.define('C', CBCTags.ItemCBC.INGOT_CAST_IRON).define('I', CBCTags.ItemCBC.INGOT_IRON)
			.pattern("C")
			.pattern("I")
			.unlockedBy("has_cast_iron", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.FLAK_AUTOCANNON_ROUND.get(), 2)
			.define('S', CBCItems.SHOT_BALLS.get()).define('G', CBCTags.ItemCBC.GUNPOWDER).define('C', CBCTags.ItemCBC.SHEET_IRON)
			.pattern("S")
			.pattern("G")
			.pattern("C")
			.unlockedBy(getHasName(CBCItems.SHOT_BALLS.get()), has(CBCItems.SHOT_BALLS.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_LOADER.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.ItemCBC.GUNPOWDER).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("G")
			.pattern("C")
			.pattern("P")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.RAM_HEAD.get())
			.define('H', ItemTags.PLANKS).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("H")
			.pattern("P")
			.unlockedBy(getHasName(CBCBlocks.CANNON_LOADER.get()), has(CBCBlocks.CANNON_LOADER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.WORM_HEAD.get())
			.define('H', Items.IRON_BARS).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("HHH")
			.pattern(" P ")
			.unlockedBy(getHasName(CBCBlocks.CANNON_LOADER.get()), has(CBCBlocks.CANNON_LOADER.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_BUILDER.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('H', CBCTags.ItemCBC.SHEET_IRON).define('h', CBCTags.ItemCBC.NUGGET_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
			.pattern("hHh")
			.pattern(" C ")
			.pattern(" P ")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_DRILL.get())
			.define('C', AllBlocks.ANDESITE_CASING.get()).define('D', CBCTags.ItemCBC.INGOT_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get()).define('p', AllBlocks.FLUID_PIPE.get())
			.pattern(" D ")
			.pattern("pCp")
			.pattern(" P ")
			.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.BASIN_FOUNDRY_LID.get())
			.define('A', AllItems.ANDESITE_ALLOY.get())
			.pattern(" A ")
			.pattern("AAA")
			.unlockedBy(getHasName(AllBlocks.BASIN.get()), has(AllBlocks.BASIN.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.QUICKFIRING_MECHANISM.get())
			.define('L', Items.LEVER).define('c', AllBlocks.COGWHEEL.get()).define('C', AllBlocks.LARGE_COGWHEEL.get())
			.pattern(" L")
			.pattern("cC")
			.unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.LOG_CANNON_END.get())
			.define('L', ItemTags.LOGS).define('K', ItemTags.WOODEN_BUTTONS).define('G', CBCTags.ItemCBC.GUNPOWDER)
			.pattern(" K ")
			.pattern("LLL")
			.pattern(" G ")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.LOG_CANNON_CHAMBER.get())
			.define('L', ItemTags.LOGS).define('G', CBCTags.ItemCBC.GUNPOWDER)
			.pattern(" L ")
			.pattern("LGL")
			.pattern(" L ")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.WROUGHT_IRON_CANNON_END.get())
			.define('I', CBCTags.ItemCBC.SHEET_IRON).define('K', CBCTags.ItemCBC.INGOT_IRON).define('G', CBCTags.ItemCBC.GUNPOWDER)
			.pattern(" K ")
			.pattern("III")
			.pattern(" G ")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.get())
			.define('I', CBCTags.ItemCBC.SHEET_IRON).define('G', CBCTags.ItemCBC.GUNPOWDER)
			.pattern(" I ")
			.pattern("IGI")
			.pattern(" I ")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_MOUNT.get())
			.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.ItemCBC.GUNPOWDER).define('I', CBCTags.ItemCBC.SHEET_IRON)
			.pattern("ISI")
			.pattern("SCS")
			.pattern("GSG")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.YAW_CONTROLLER.get())
			.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', CBCTags.ItemCBC.GUNPOWDER)
			.pattern(" S ")
			.pattern("GCG")
			.pattern(" S ")
			.unlockedBy(getHasName(CBCBlocks.CANNON_MOUNT.get()), has(CBCBlocks.CANNON_MOUNT.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.CAST_IRON_SLIDING_BREECHBLOCK.get())
			.define('I', CBCTags.ItemCBC.INGOT_CAST_IRON).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_cast_iron_ingot", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.BRONZE_SLIDING_BREECHBLOCK.get())
			.define('I', CBCTags.ItemCBC.INGOT_BRONZE).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_bronze_ingot", has(CBCTags.ItemCBC.INGOT_BRONZE))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.STEEL_SLIDING_BREECHBLOCK.get())
			.define('I', CBCTags.ItemCBC.INGOT_STEEL).define('C', AllBlocks.COGWHEEL.get())
			.pattern(" I ")
			.pattern("CIC")
			.pattern(" I ")
			.unlockedBy("has_steel_ingot", has(CBCTags.ItemCBC.INGOT_STEEL))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.STEEL_SCREW_LOCK.get())
			.define('I', CBCTags.ItemCBC.INGOT_STEEL).define('S', AllBlocks.SHAFT.get())
			.pattern(" S ")
			.pattern("III")
			.unlockedBy("has_steel_ingot", has(CBCTags.ItemCBC.INGOT_STEEL))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.NETHERSTEEL_SCREW_LOCK.get())
			.define('I', CBCItems.NETHERSTEEL_INGOT.get()).define('S', AllBlocks.SHAFT.get())
			.pattern(" S ")
			.pattern("III")
			.unlockedBy(getHasName(CBCItems.NETHERSTEEL_INGOT.get()), has(CBCItems.NETHERSTEEL_INGOT.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.RAM_ROD.get())
			.define('H', CBCBlocks.RAM_HEAD.get()).define('S', Items.STICK)
			.pattern("H")
			.pattern("S")
			.pattern("S")
			.unlockedBy(getHasName(CBCBlocks.RAM_HEAD.get()), has(CBCBlocks.RAM_HEAD.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.WORM.get())
			.define('H', CBCBlocks.WORM_HEAD.get()).define('S', Items.STICK)
			.pattern("H")
			.pattern("S")
			.pattern("S")
			.unlockedBy(getHasName(CBCBlocks.WORM_HEAD.get()), has(CBCBlocks.WORM_HEAD.get()))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.PAIR_OF_CANNON_WHEELS.get(), 2)
			.define('S', CBCTags.ItemCBC.SHEET_IRON).define('L', ItemTags.LOGS)
			.pattern(" S ")
			.pattern("SLS")
			.pattern(" S ")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_CARRIAGE.get())
			.define('P', ItemTags.PLANKS).define('S', AllBlocks.SHAFT.get()).define('W', CBCItems.PAIR_OF_CANNON_WHEELS.get())
			.pattern(" SP")
			.pattern("PPP")
			.pattern("W W")
			.unlockedBy("has_gunpowder", has(CBCTags.ItemCBC.GUNPOWDER))
			.save(cons);

		SpecialRecipeBuilder.special(CBCRecipeTypes.MUNITION_FUZING.getSerializer()).save(cons, "munition_fuzing");
		SpecialRecipeBuilder.special(CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer()).save(cons, "cartridge_assembly");
		SpecialRecipeBuilder.special(CBCRecipeTypes.BIG_CARTRIDGE_FILLING.getSerializer()).save(cons, "big_cartridge_filling");
		SpecialRecipeBuilder.special(CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getSerializer()).save(cons, "big_cartridge_filling_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getSerializer()).save(cons, "munition_fuzing_deployer");
		SpecialRecipeBuilder.special(CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer()).save(cons, "cartridge_assembly_deployer");

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(CBCItems.CONGEALED_NITRO.get()), CBCItems.HARDENED_NITRO.get(), 5, 200)
			.unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
			.save(cons);
	}

}
