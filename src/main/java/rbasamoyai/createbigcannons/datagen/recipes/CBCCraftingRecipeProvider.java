package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import rbasamoyai.createbigcannons.*;

import java.util.function.Consumer;

public class CBCCraftingRecipeProvider extends RecipeProvider {

	public CBCCraftingRecipeProvider(DataGenerator gen) {
		super(gen);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> cons) {
		TagKey<Item> ironSheetTag = AllTags.forgeItemTag("plates/iron");

		ShapedRecipeBuilder.shaped(CBCItems.IMPACT_FUZE.get())
		.define('T', CBCTags.ItemCBC.IMPACT_FUZE_HEAD).define('R', Tags.Items.DUSTS_REDSTONE)
		.pattern("T")
		.pattern("R")
		.unlockedBy("has_impact_fuze_head", has(CBCTags.ItemCBC.IMPACT_FUZE_HEAD))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCItems.TIMED_FUZE.get())
		.define('I', Tags.Items.INGOTS_IRON).define('C', Items.CLOCK).define('R', Tags.Items.DUSTS_REDSTONE)
		.pattern("I")
		.pattern("C")
		.pattern("R")
		.unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCItems.PROXIMITY_FUZE.get())
		.define('A', Items.IRON_BARS).define('C', Tags.Items.GEMS_QUARTZ).define('R', Tags.Items.DUSTS_REDSTONE).define('I', Tags.Items.INGOTS_IRON)
		.pattern(" A ")
		.pattern("RCR")
		.pattern(" I ")
		.unlockedBy(getHasName(Items.IRON_BARS), has(Items.IRON_BARS))
		.unlockedBy("has_quartz", has(Tags.Items.GEMS_QUARTZ))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCItems.EMPTY_POWDER_CHARGE.get())
		.define('W', ItemTags.WOOL).define('S', Items.STRING)
		.pattern("S")
		.pattern("W")
		.pattern("S")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapelessRecipeBuilder.shapeless(CBCBlocks.POWDER_CHARGE.get())
		.requires(CBCItems.PACKED_GUNPOWDER.get())
		.requires(CBCItems.EMPTY_POWDER_CHARGE.get())
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
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
		.define('I', Tags.Items.INGOTS_IRON).define('i', Tags.Items.NUGGETS_IRON).define('S', ItemTags.WOODEN_SLABS)
		.pattern("iIi")
		.pattern("III")
		.pattern(" S ")
		.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
		.unlockedBy("has_cast_iron_ingot", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.SOLID_SHOT.get())
		.define('I', CBCTags.ItemCBC.INGOT_CAST_IRON).define('S', ItemTags.WOODEN_SLABS)
		.pattern("I")
		.pattern("I")
		.pattern("S")
		.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
		.unlockedBy("has_cast_iron_ingot", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
		.save(cons, CreateBigCannons.resource("solid_shot_cast_iron"));

		ShapedRecipeBuilder.shaped(CBCBlocks.MORTAR_STONE.get())
		.define('S', Tags.Items.STONE).define('s', ItemTags.WOODEN_SLABS)
		.pattern(" S ")
		.pattern("SSS")
		.pattern(" s ")
		.unlockedBy("has_stone", has(Tags.Items.STONE))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCItems.SHOT_BALLS.get(), 2)
		.define('i', Tags.Items.NUGGETS_IRON).define('I', Tags.Items.INGOTS_IRON)
		.pattern("iii")
		.pattern("iIi")
		.pattern("iii")
		.unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
		.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.BAG_OF_GRAPESHOT.get())
		.define('D', Items.STRING).define('W', ItemTags.WOOL).define('L', CBCItems.SHOT_BALLS.get()).define('S', ItemTags.WOODEN_SLABS)
		.pattern("DDD")
		.pattern("WLW")
		.pattern(" S ")
		.unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
		.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
		.save(cons);
		
		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.SHRAPNEL_SHELL.get())
		.key('I', Tags.Items.INGOTS_IRON).key('L', CBCItems.SHOT_BALLS.get()).key('S', ItemTags.WOODEN_SLABS).key('P', Tags.Items.GUNPOWDER)
		.patternLine(" I ")
		.patternLine("ILI")
		.patternLine("IPI")
		.patternLine(" S ")
		.build(cons);
		
		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.HE_SHELL.get())
		.key('I', Tags.Items.INGOTS_IRON).key('T', Items.TNT).key('S', ItemTags.WOODEN_SLABS)
		.patternLine(" I ")
		.patternLine("ITI")
		.patternLine("ITI")
		.patternLine(" S ")
		.build(cons);
		
		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.AP_SHELL.get())
		.key('I', Tags.Items.INGOTS_IRON).key('C', CBCTags.ItemCBC.INGOT_CAST_IRON).key('T', Items.TNT).key('S', ItemTags.WOODEN_SLABS)
		.patternLine(" C ")
		.patternLine("ICI")
		.patternLine("ITI")
		.patternLine(" S ")
		.build(cons);
		
		MechanicalCraftingRecipeBuilder.shapedRecipe(CBCBlocks.FLUID_SHELL.get())
		.key('I', Tags.Items.INGOTS_IRON).key('P', AllBlocks.FLUID_PIPE.get()).key('S', ItemTags.WOODEN_SLABS)
		.patternLine(" I ")
		.patternLine("IPI")
		.patternLine("IPI")
		.patternLine(" S ")
		.build(cons);

		ShapedRecipeBuilder.shaped(CBCItems.AP_AUTOCANNON_ROUND.get(), 4)
		.define('C', CBCTags.ItemCBC.INGOT_CAST_IRON).define('I', Tags.Items.INGOTS_IRON)
		.pattern("C")
		.pattern("I")
		.unlockedBy("has_cast_iron", has(CBCTags.ItemCBC.INGOT_CAST_IRON))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCItems.FLAK_AUTOCANNON_ROUND.get(), 2)
		.define('S', CBCItems.SHOT_BALLS.get()).define('G', Tags.Items.GUNPOWDER).define('C', ironSheetTag)
		.pattern("S")
		.pattern("G")
		.pattern("C")
		.unlockedBy(getHasName(CBCItems.SHOT_BALLS.get()), has(CBCItems.SHOT_BALLS.get()))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_LOADER.get())
		.define('C', AllBlocks.ANDESITE_CASING.get()).define('G', Tags.Items.GUNPOWDER).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
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
		.define('C', AllBlocks.ANDESITE_CASING.get()).define('H', ironSheetTag).define('h', Tags.Items.NUGGETS_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get())
		.pattern("hHh")
		.pattern(" C ")
		.pattern(" P ")
		.unlockedBy(getHasName(AllBlocks.PISTON_EXTENSION_POLE.get()), has(AllBlocks.PISTON_EXTENSION_POLE.get()))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_DRILL.get())
		.define('C', AllBlocks.ANDESITE_CASING.get()).define('D', Tags.Items.INGOTS_IRON).define('P', AllBlocks.PISTON_EXTENSION_POLE.get()).define('p', AllBlocks.FLUID_PIPE.get())
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
		
		ShapedRecipeBuilder.shaped(CBCBlocks.VERY_SMALL_CAST_MOULD.get())
		.define('P', ItemTags.PLANKS).define('S', Items.STICK)
		.pattern("SPS")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.SMALL_CAST_MOULD.get())
		.define('P', ItemTags.PLANKS).define('F', ItemTags.FENCES)
		.pattern("FPF")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.MEDIUM_CAST_MOULD.get())
		.define('L', ItemTags.LOGS).define('S', Items.STICK)
		.pattern("SLS")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.LARGE_CAST_MOULD.get())
		.define('L', ItemTags.LOGS).define('F', ItemTags.FENCES)
		.pattern("FLF")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.VERY_LARGE_CAST_MOULD.get())
		.define('L', ItemTags.LOGS).define('P', ItemTags.PLANKS)
		.pattern("PLP")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_END_CAST_MOULD.get())
		.define('S', ItemTags.WOODEN_SLABS).define('K', ItemTags.WOODEN_BUTTONS)
		.pattern("S")
		.pattern("K")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.SLIDING_BREECH_CAST_MOULD.get())
		.define('L', ItemTags.LOGS).define('S', ItemTags.SLABS)
		.pattern("SLS")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.SCREW_BREECH_CAST_MOULD.get())
		.define('S', ItemTags.WOODEN_SLABS).define('s', Items.STICK)
		.pattern("S")
		.pattern("s")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.AUTOCANNON_BREECH_CAST_MOULD.get())
		.define('S', ItemTags.WOODEN_FENCES).define('s', Items.STICK)
		.pattern(" s ")
		.pattern("sSs")
		.pattern(" s ")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.AUTOCANNON_RECOIL_SPRING_CAST_MOULD.get())
		.define('S', ItemTags.WOODEN_FENCES).define('s', Items.STICK)
		.pattern("s")
		.pattern("S")
		.pattern("s")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.AUTOCANNON_BARREL_CAST_MOULD.get())
		.define('s', Items.STICK)
		.pattern("s")
		.pattern("s")
		.pattern("s")
		.unlockedBy(getHasName(CBCBlocks.CASTING_SAND.get()), has(CBCBlocks.CASTING_SAND.get()))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.LOG_CANNON_END.get())
		.define('L', ItemTags.LOGS).define('K', ItemTags.WOODEN_BUTTONS).define('G', Tags.Items.GUNPOWDER)
		.pattern(" K ")
		.pattern("LLL")
		.pattern(" G ")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.LOG_CANNON_CHAMBER.get())
		.define('L', ItemTags.LOGS).define('G', Tags.Items.GUNPOWDER)
		.pattern(" L ")
		.pattern("LGL")
		.pattern(" L ")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.WROUGHT_IRON_CANNON_END.get())
		.define('I', ironSheetTag).define('K', Tags.Items.INGOTS_IRON).define('G', Tags.Items.GUNPOWDER)
		.pattern(" K ")
		.pattern("III")
		.pattern(" G ")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.get())
		.define('I', ironSheetTag).define('G', Tags.Items.GUNPOWDER)
		.pattern(" I ")
		.pattern("IGI")
		.pattern(" I ")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_MOUNT.get())
		.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', Tags.Items.GUNPOWDER).define('I', ironSheetTag)
		.pattern("ISI")
		.pattern("SCS")
		.pattern("GSG")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);
		
		ShapedRecipeBuilder.shaped(CBCBlocks.YAW_CONTROLLER.get())
		.define('S', AllBlocks.SHAFT.get()).define('C', AllBlocks.ANDESITE_CASING.get()).define('G', Tags.Items.GUNPOWDER)
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
		.define('S', ironSheetTag).define('L', ItemTags.LOGS)
		.pattern(" S ")
		.pattern("SLS")
		.pattern(" S ")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);

		ShapedRecipeBuilder.shaped(CBCBlocks.CANNON_CARRIAGE.get())
		.define('P', ItemTags.PLANKS).define('S', AllBlocks.SHAFT.get()).define('W', CBCItems.PAIR_OF_CANNON_WHEELS.get())
		.pattern(" SP")
		.pattern("PPP")
		.pattern("W W")
		.unlockedBy("has_gunpowder", has(Tags.Items.GUNPOWDER))
		.save(cons);

		SpecialRecipeBuilder.special(CBCRecipeTypes.MUNITION_FUZING.getSerializer()).save(cons, "munition_fuzing");
		SpecialRecipeBuilder.special(CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer()).save(cons, "cartridge_assembly");
	}
	
	@Override public String getName() { return "Create Big Cannons Recipes: Crafting"; }
	
}
