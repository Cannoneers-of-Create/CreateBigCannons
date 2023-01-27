package rbasamoyai.createbigcannons;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannonloading.RamHeadBlock;
import rbasamoyai.createbigcannons.cannonloading.WormHeadBlock;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlock;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockItem;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonTubeBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.*;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonEndBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.ScrewBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.SlidingBreechBlock;
import rbasamoyai.createbigcannons.crafting.boring.*;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpCannonBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderHeadBlock;
import rbasamoyai.createbigcannons.crafting.builtup.BigCannonLayerBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlock;
import rbasamoyai.createbigcannons.crafting.foundry.BasinFoundryLidBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteSlidingBreechBlock;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.munitions.big_cannon.PowderChargeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellBlock;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Supplier;

public class CBCBlocks {

	static {
		REGISTRATE.creativeModeTab(() -> ModGroup.GROUP);
	}

	static {
		REGISTRATE.startSection(AllSections.KINETICS);
	}
	
	//////// Log cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> LOG_CANNON_CHAMBER = REGISTRATE
			.block("log_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.LOG))
			.transform(logCannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/log"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonEndBlock> LOG_CANNON_END = REGISTRATE
			.block("log_cannon_end", p -> new BigCannonEndBlock(p, BigCannonMaterial.LOG))
			.transform(logCannonBlock())
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/log"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	//////// Wrought Iron cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> WROUGHT_IRON_CANNON_CHAMBER = REGISTRATE
			.block("wrought_iron_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.WROUGHT_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/wrought_iron"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonEndBlock> WROUGHT_IRON_CANNON_END = REGISTRATE
			.block("wrought_iron_cannon_end", p -> new BigCannonEndBlock(p, BigCannonMaterial.WROUGHT_IRON))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/wrought_iron"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	//////// Cast Iron cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> CAST_IRON_CANNON_BARREL = REGISTRATE
			.block("cast_iron_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, BigCannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> CAST_IRON_CANNON_CHAMBER = REGISTRATE
			.block("cast_iron_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonEndBlock> CAST_IRON_CANNON_END = REGISTRATE
			.block("cast_iron_cannon_end", p -> new BigCannonEndBlock(p, BigCannonMaterial.CAST_IRON))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("cast_iron_sliding_breech", p -> new SlidingBreechBlock(p, BigCannonMaterial.CAST_IRON))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(10))
			.transform(BlockStressDefaults.setImpact(16.0d))
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_CAST_IRON_CANNON_BARREL = REGISTRATE
			.block("unbored_cast_iron_cannon_barrel", p -> UnboredBigCannonBlock.verySmall(p, BigCannonMaterial.CAST_IRON, CAST_IRON_CANNON_BARREL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/cast_iron", "cannon_barrel/unbored_cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_CAST_IRON_CANNON_CHAMBER = REGISTRATE
			.block("unbored_cast_iron_cannon_chamber", p -> UnboredBigCannonBlock.medium(p, BigCannonMaterial.CAST_IRON, CAST_IRON_CANNON_CHAMBER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/cast_iron", "cannon_chamber/unbored_cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("incomplete_cast_iron_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, BigCannonMaterial.CAST_IRON, CBCItems.CAST_IRON_SLIDING_BREECHBLOCK, CAST_IRON_SLIDING_BREECH))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("unbored_cast_iron_sliding_breech", p -> new UnboredSlidingBreechBlock(p, BigCannonMaterial.CAST_IRON, INCOMPLETE_CAST_IRON_SLIDING_BREECH, Shapes.block()))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_cast_iron"))
			.loot(CBCBuilderTransformers.castIronScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	//////// Bronze cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> BRONZE_CANNON_BARREL = REGISTRATE
			.block("bronze_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, BigCannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> BRONZE_CANNON_CHAMBER = REGISTRATE
			.block("bronze_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonEndBlock> BRONZE_CANNON_END = REGISTRATE
			.block("bronze_cannon_end", p -> new BigCannonEndBlock(p, BigCannonMaterial.BRONZE))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> BRONZE_SLIDING_BREECH = REGISTRATE
			.block("bronze_sliding_breech", p -> new SlidingBreechBlock(p, BigCannonMaterial.BRONZE))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
			.transform(BlockStressDefaults.setImpact(12.0d))
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_BRONZE_CANNON_BARREL = REGISTRATE
			.block("unbored_bronze_cannon_barrel", p -> UnboredBigCannonBlock.verySmall(p, BigCannonMaterial.BRONZE, BRONZE_CANNON_BARREL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/bronze", "cannon_barrel/unbored_bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_BRONZE_CANNON_CHAMBER = REGISTRATE
			.block("unbored_bronze_cannon_chamber", p -> UnboredBigCannonBlock.medium(p, BigCannonMaterial.BRONZE, BRONZE_CANNON_CHAMBER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/bronze", "cannon_chamber/unbored_bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_BRONZE_SLIDING_BREECH = REGISTRATE
			.block("incomplete_bronze_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, BigCannonMaterial.BRONZE, CBCItems.BRONZE_SLIDING_BREECHBLOCK, BRONZE_SLIDING_BREECH))
			.transform(cannonBlock(false))
			.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_BRONZE_SLIDING_BREECH = REGISTRATE
			.block("unbored_bronze_sliding_breech", p -> new UnboredSlidingBreechBlock(p, BigCannonMaterial.BRONZE, INCOMPLETE_BRONZE_SLIDING_BREECH, Shapes.block()))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_bronze"))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	//////// Steel cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> STEEL_CANNON_BARREL = REGISTRATE
			.block("steel_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_STEEL_CANNON_BARREL = REGISTRATE
			.block("built_up_steel_cannon_barrel", p -> BigCannonTubeBlock.small(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_barrel", "cannon_tubing/steel"))
			.tag(CBCTags.BlockCBC.REDUCES_SPREAD)
			.lang("Built-Up Steel Cannon Barrel")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> STEEL_CANNON_CHAMBER = REGISTRATE
			.block("steel_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_STEEL_CANNON_CHAMBER = REGISTRATE
			.block("built_up_steel_cannon_chamber", p -> BigCannonTubeBlock.large(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_chamber", "cannon_tubing/steel"))
			.tag(CBCTags.BlockCBC.THICK_TUBING)
			.lang("Built-Up Steel Cannon Chamber")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> THICK_STEEL_CANNON_CHAMBER = REGISTRATE
			.block("thick_steel_cannon_chamber", p -> BigCannonTubeBlock.veryLarge(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("thick_cannon_chamber", "cannon_tubing/steel"))
			.tag(CBCTags.BlockCBC.THICK_TUBING)
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> VERY_SMALL_STEEL_CANNON_LAYER = REGISTRATE
			.block("very_small_steel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.STEEL, CannonCastShape.VERY_SMALL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("very_small", "cannon_tubing/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> SMALL_STEEL_CANNON_LAYER = REGISTRATE
			.block("small_steel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.STEEL, CannonCastShape.SMALL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("small", "cannon_tubing/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> MEDIUM_STEEL_CANNON_LAYER = REGISTRATE
			.block("medium_steel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.STEEL, CannonCastShape.MEDIUM))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("medium", "cannon_tubing/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> LARGE_STEEL_CANNON_LAYER = REGISTRATE
			.block("large_steel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.STEEL, CannonCastShape.LARGE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("large", "cannon_tubing/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> VERY_LARGE_STEEL_CANNON_LAYER = REGISTRATE
			.block("very_large_steel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.STEEL, CannonCastShape.VERY_LARGE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("very_large", "cannon_tubing/steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();	
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_SMALL_STEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_very_small_steel_cannon_layer", p -> UnboredBigCannonBlock.verySmall(p, BigCannonMaterial.STEEL, VERY_SMALL_STEEL_CANNON_LAYER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/steel", "cannon_barrel/unbored_steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_SMALL_STEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_small_steel_cannon_layer", p -> UnboredBigCannonBlock.small(p, BigCannonMaterial.STEEL, SMALL_STEEL_CANNON_LAYER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_barrel", "cannon_tubing/unbored_steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_MEDIUM_STEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_medium_steel_cannon_layer", p -> UnboredBigCannonBlock.medium(p, BigCannonMaterial.STEEL, MEDIUM_STEEL_CANNON_LAYER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/steel", "cannon_chamber/unbored_steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_LARGE_STEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_large_steel_cannon_layer", p -> UnboredBigCannonBlock.large(p, BigCannonMaterial.STEEL, LARGE_STEEL_CANNON_LAYER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_chamber", "cannon_tubing/unbored_steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_LARGE_STEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_very_large_steel_cannon_layer", p -> UnboredBigCannonBlock.veryLarge(p, BigCannonMaterial.STEEL, VERY_LARGE_STEEL_CANNON_LAYER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("thick_cannon_chamber", "cannon_tubing/unbored_steel"))
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> STEEL_SLIDING_BREECH = REGISTRATE
			.block("steel_sliding_breech", p -> new SlidingBreechBlock(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/steel"))
			.transform(BlockStressDefaults.setImpact(32.0d))
			.register();
	
	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_STEEL_SLIDING_BREECH = REGISTRATE
			.block("incomplete_steel_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, BigCannonMaterial.STEEL, CBCItems.STEEL_SLIDING_BREECHBLOCK, STEEL_SLIDING_BREECH))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/steel"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_STEEL_SLIDING_BREECH = REGISTRATE
			.block("unbored_steel_sliding_breech", p -> new UnboredSlidingBreechBlock(p, BigCannonMaterial.STEEL, INCOMPLETE_STEEL_SLIDING_BREECH, Shapes.block()))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_steel"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<ScrewBreechBlock> STEEL_SCREW_BREECH = REGISTRATE
			.block("steel_screw_breech", p -> new ScrewBreechBlock(p, BigCannonMaterial.STEEL))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCBuilderTransformers.screwBreech("screw_breech/steel"))
			.transform(BlockStressDefaults.setImpact(16.0d))
			.register();
	
	public static final BlockEntry<IncompleteScrewBreechBlock> INCOMPLETE_STEEL_SCREW_BREECH = REGISTRATE
			.block("incomplete_steel_screw_breech", p -> new IncompleteScrewBreechBlock(p, BigCannonMaterial.STEEL, CBCItems.STEEL_SCREW_LOCK, STEEL_SCREW_BREECH))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCBuilderTransformers.screwBreechIncomplete("screw_breech/steel"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredScrewBreechBlock> UNBORED_STEEL_SCREW_BREECH = REGISTRATE
			.block("unbored_steel_screw_breech", p -> new UnboredScrewBreechBlock(p, BigCannonMaterial.STEEL, INCOMPLETE_STEEL_SCREW_BREECH))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(15))
			.transform(CBCBuilderTransformers.screwBreechUnbored("screw_breech/steel", "screw_breech/unbored_steel"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	//////// Nethersteel cannon blocks ////////
	
	public static final BlockEntry<BigCannonTubeBlock> NETHERSTEEL_CANNON_BARREL = REGISTRATE
			.block("nethersteel_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_NETHERSTEEL_CANNON_BARREL = REGISTRATE
			.block("built_up_nethersteel_cannon_barrel", p -> BigCannonTubeBlock.small(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_barrel", "cannon_tubing/nethersteel"))
			.tag(CBCTags.BlockCBC.REDUCES_SPREAD)
			.lang("Built-Up Nethersteel Cannon Barrel")
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
			.block("nethersteel_cannon_chamber", p -> BigCannonTubeBlock.medium(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
			.block("built_up_nethersteel_cannon_chamber", p -> BigCannonTubeBlock.large(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_chamber", "cannon_tubing/nethersteel"))
			.tag(CBCTags.BlockCBC.THICK_TUBING)
			.lang("Built-Up Nethersteel Cannon Chamber")
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonTubeBlock> THICK_NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
			.block("thick_nethersteel_cannon_chamber", p -> BigCannonTubeBlock.veryLarge(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("thick_cannon_chamber", "cannon_tubing/nethersteel"))
			.tag(CBCTags.BlockCBC.THICK_TUBING)
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> VERY_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("very_small_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.NETHERSTEEL, CannonCastShape.VERY_SMALL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("very_small", "cannon_tubing/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("small_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.NETHERSTEEL, CannonCastShape.SMALL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("small", "cannon_tubing/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> MEDIUM_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("medium_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.NETHERSTEEL, CannonCastShape.MEDIUM))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("medium", "cannon_tubing/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("large_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.NETHERSTEEL, CannonCastShape.LARGE))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("large", "cannon_tubing/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<BigCannonLayerBlock> VERY_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("very_large_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, BigCannonMaterial.NETHERSTEEL, CannonCastShape.VERY_LARGE))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedHollowCannon("very_large", "cannon_tubing/nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
			.item(BigCannonBlockItem::new).build()
			.register();	
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_very_small_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.verySmall(p, BigCannonMaterial.NETHERSTEEL, VERY_SMALL_NETHERSTEEL_CANNON_LAYER))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/nethersteel", "cannon_barrel/unbored_nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_small_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.small(p, BigCannonMaterial.NETHERSTEEL, SMALL_NETHERSTEEL_CANNON_LAYER))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_barrel", "cannon_tubing/unbored_nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_medium_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.medium(p, BigCannonMaterial.NETHERSTEEL, MEDIUM_NETHERSTEEL_CANNON_LAYER))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/nethersteel", "cannon_chamber/unbored_nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_large_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.large(p, BigCannonMaterial.NETHERSTEEL, LARGE_NETHERSTEEL_CANNON_LAYER))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("built_up_cannon_chamber", "cannon_tubing/unbored_nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
			.block("unbored_very_large_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.veryLarge(p, BigCannonMaterial.NETHERSTEEL, VERY_LARGE_NETHERSTEEL_CANNON_LAYER))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.sizedCannon("thick_cannon_chamber", "cannon_tubing/unbored_nethersteel"))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<ScrewBreechBlock> NETHERSTEEL_SCREW_BREECH = REGISTRATE
			.block("nethersteel_screw_breech", p -> new ScrewBreechBlock(p, BigCannonMaterial.NETHERSTEEL))
			.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.transform(CBCBuilderTransformers.screwBreech("screw_breech/nethersteel"))
			.transform(BlockStressDefaults.setImpact(40.0d))
			.register();
	
	public static final BlockEntry<IncompleteScrewBreechBlock> INCOMPLETE_NETHERSTEEL_SCREW_BREECH = REGISTRATE
			.block("incomplete_nethersteel_screw_breech", p -> new IncompleteScrewBreechBlock(p, BigCannonMaterial.NETHERSTEEL, CBCItems.NETHERSTEEL_SCREW_LOCK, NETHERSTEEL_SCREW_BREECH))
			.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
			.transform(CBCBuilderTransformers.screwBreechIncomplete("screw_breech/nethersteel"))
			.item(BigCannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredScrewBreechBlock> UNBORED_NETHERSTEEL_SCREW_BREECH = REGISTRATE
			.block("unbored_nethersteel_screw_breech", p -> new UnboredScrewBreechBlock(p, BigCannonMaterial.NETHERSTEEL, INCOMPLETE_NETHERSTEEL_SCREW_BREECH))
			.transform(strongCannonBlock())
			.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
			.transform(CBCBuilderTransformers.screwBreechUnbored("screw_breech/nethersteel", "screw_breech/unbored_nethersteel"))
			.item(BigCannonBlockItem::new).build()
			.register();

	//////// Cast iron autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> CAST_IRON_AUTOCANNON_BARREL = REGISTRATE
			.block("cast_iron_autocannon_barrel", p -> new AutocannonBarrelBlock(p, AutocannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(2))
			.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/cast_iron"))
			.register();

	public static final BlockEntry<AutocannonBreechBlock> CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
			.block("cast_iron_autocannon_breech", p -> new AutocannonBreechBlock(p, AutocannonMaterial.CAST_IRON))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.castIronScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/cast_iron", true))
			.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("cast_iron_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, AutocannonMaterial.CAST_IRON))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.castIronScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/cast_iron", true))
			.register();
	
	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("incomplete_cast_iron_autocannon_recoil_spring", p ->
					IncompleteAutocannonBlock.recoilSpring(p, AutocannonMaterial.CAST_IRON, CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/cast_iron", false))
			.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
			.block("incomplete_cast_iron_autocannon_breech", p ->
					IncompleteAutocannonBlock.breech(p, AutocannonMaterial.CAST_IRON, CAST_IRON_AUTOCANNON_BREECH, CBCItems.CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/cast_iron", false))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_BARREL = REGISTRATE
			.block("unbored_cast_iron_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, AutocannonMaterial.CAST_IRON, CAST_IRON_AUTOCANNON_BARREL))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(4))
			.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/cast_iron"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("unbored_cast_iron_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, AutocannonMaterial.CAST_IRON, INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(6))
			.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/cast_iron"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
			.block("unbored_cast_iron_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, AutocannonMaterial.CAST_IRON, INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.castIronScrapLoot(8))
			.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/cast_iron"))
			.register();
	
	//////// Bronze autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> BRONZE_AUTOCANNON_BARREL = REGISTRATE
			.block("bronze_autocannon_barrel", p -> new AutocannonBarrelBlock(p, AutocannonMaterial.BRONZE))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(2))
			.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/bronze"))
			.register();

	public static final BlockEntry<AutocannonBreechBlock> BRONZE_AUTOCANNON_BREECH = REGISTRATE
			.block("bronze_autocannon_breech", p -> new AutocannonBreechBlock(p, AutocannonMaterial.BRONZE))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/bronze", true))
			.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("bronze_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, AutocannonMaterial.BRONZE))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.bronzeScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/bronze", true))
			.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("incomplete_bronze_autocannon_recoil_spring", p ->
					IncompleteAutocannonBlock.recoilSpring(p, AutocannonMaterial.BRONZE, BRONZE_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/bronze", false))
			.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_BRONZE_AUTOCANNON_BREECH = REGISTRATE
			.block("incomplete_bronze_autocannon_breech", p ->
					IncompleteAutocannonBlock.breech(p, AutocannonMaterial.BRONZE, BRONZE_AUTOCANNON_BREECH, CBCItems.BRONZE_AUTOCANNON_BREECH_EXTRACTOR))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/bronze", false))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_BARREL = REGISTRATE
			.block("unbored_bronze_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, AutocannonMaterial.BRONZE, BRONZE_AUTOCANNON_BARREL))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
			.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/bronze"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("unbored_bronze_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, AutocannonMaterial.BRONZE, INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(6))
			.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/bronze"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_BREECH = REGISTRATE
			.block("unbored_bronze_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, AutocannonMaterial.BRONZE, INCOMPLETE_BRONZE_AUTOCANNON_BREECH))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.bronzeScrapLoot(8))
			.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/bronze"))
			.register();

	//////// Steel autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> STEEL_AUTOCANNON_BARREL = REGISTRATE
			.block("steel_autocannon_barrel", p -> new AutocannonBarrelBlock(p, AutocannonMaterial.STEEL))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(2))
			.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/steel"))
			.register();

	public static final BlockEntry<AutocannonBreechBlock> STEEL_AUTOCANNON_BREECH = REGISTRATE
			.block("steel_autocannon_breech", p -> new AutocannonBreechBlock(p, AutocannonMaterial.STEEL))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/steel", true))
			.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("steel_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, AutocannonMaterial.STEEL))
			.transform(cannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/steel", true))
			.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("incomplete_steel_autocannon_recoil_spring", p ->
					IncompleteAutocannonBlock.recoilSpring(p, AutocannonMaterial.STEEL, STEEL_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(3))
			.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/steel", false))
			.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_STEEL_AUTOCANNON_BREECH = REGISTRATE
			.block("incomplete_steel_autocannon_breech", p ->
					IncompleteAutocannonBlock.breech(p, AutocannonMaterial.STEEL, STEEL_AUTOCANNON_BREECH, CBCItems.STEEL_AUTOCANNON_BREECH_EXTRACTOR))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(4))
			.transform(CBCBuilderTransformers.autocannonBreech("autocannon/steel", false))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_BARREL = REGISTRATE
			.block("unbored_steel_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, AutocannonMaterial.STEEL, STEEL_AUTOCANNON_BARREL))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(4))
			.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/steel"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.block("unbored_steel_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, AutocannonMaterial.STEEL, INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(6))
			.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/steel"))
			.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_BREECH = REGISTRATE
			.block("unbored_steel_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, AutocannonMaterial.STEEL, INCOMPLETE_STEEL_AUTOCANNON_BREECH))
			.transform(cannonBlock())
			.loot(CBCBuilderTransformers.steelScrapLoot(8))
			.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/steel"))
			.register();
	
	//////// Cannon loading blocks ////////
	
	public static final BlockEntry<CannonLoaderBlock> CANNON_LOADER = REGISTRATE
			.block("cannon_loader", CannonLoaderBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonLoader())
			.transform(BlockStressDefaults.setImpact(8.0d))
			.register();
	
	public static final BlockEntry<RamHeadBlock> RAM_HEAD = REGISTRATE
			.block("ram_head", RamHeadBlock::new)
			.initialProperties(() -> Blocks.PISTON_HEAD)
			.properties(p -> p.sound(SoundType.WOOD))
			.transform(CBCBuilderTransformers.ramHead())
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();
	
	public static final BlockEntry<WormHeadBlock> WORM_HEAD = REGISTRATE
			.block("worm_head", WormHeadBlock::new)
			.initialProperties(() -> Blocks.PISTON_HEAD)
			.properties(p -> p.sound(SoundType.METAL))
			.transform(CBCBuilderTransformers.wormHead())
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();
	
	//////// Cannon mount blocks ////////
		
	public static final BlockEntry<CannonMountBlock> CANNON_MOUNT = REGISTRATE
			.block("cannon_mount", CannonMountBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.properties(p -> p.isRedstoneConductor(CBCBlocks::never))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonMount())
			.register();
	
	public static final BlockEntry<YawControllerBlock> YAW_CONTROLLER = REGISTRATE
			.block("yaw_controller", YawControllerBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.yawController())
			.register();

	public static final BlockEntry<CannonCarriageBlock> CANNON_CARRIAGE = REGISTRATE
			.block("cannon_carriage", CannonCarriageBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.properties(p -> p.isRedstoneConductor(CBCBlocks::never))
			.transform(CBCBuilderTransformers.cannonCarriage())
			.register();
	
	//////// Cannon crafting mechanism blocks ////////
	
	public static final BlockEntry<CannonDrillBlock> CANNON_DRILL = REGISTRATE
			.block("cannon_drill", CannonDrillBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonDrill())
			.transform(BlockStressDefaults.setImpact(8.0d))
			.register();
	
	public static final BlockEntry<DrillBitBlock> CANNON_DRILL_BIT = REGISTRATE
			.block("cannon_drill_bit", DrillBitBlock::new)
			.initialProperties(() -> Blocks.PISTON_HEAD)
			.properties(p -> p.color(MaterialColor.STONE))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonDrillBit())
			.register();
	
	public static final BlockEntry<CannonBuilderBlock> CANNON_BUILDER = REGISTRATE
			.block("cannon_builder", CannonBuilderBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonBuilder())
			.transform(BlockStressDefaults.setImpact(8.0d))
			.register();
	
	public static final BlockEntry<CannonBuilderHeadBlock> CANNON_BUILDER_HEAD = REGISTRATE
			.block("cannon_builder_head", CannonBuilderHeadBlock::new)
			.initialProperties(() -> Blocks.PISTON_HEAD)
			.properties(p -> p.color(MaterialColor.STONE))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonBuilderHead())
			.register();
	
	public static final BlockEntry<BuiltUpCannonBlock> BUILT_UP_CANNON = REGISTRATE
			.block("built_up_cannon", BuiltUpCannonBlock::new)
			.transform(strongCannonBlock())
			.properties(p -> p.noOcclusion())
			.blockstate((c, p) -> {
				ResourceLocation texLoc = CreateBigCannons.resource("block/cannon_chamber/steel_cannon_chamber_side");
				p.directionalBlock(c.get(), p.models().getBuilder(c.getName()).texture("particle", texLoc));
			})
			.register();
	
	static {
		REGISTRATE.startSection(AllSections.LOGISTICS);
	}
	
	public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE
			.block("cast_iron_block", Block::new)
			.initialProperties(Material.METAL)
			.lang("Block of Cast Iron")
			.item()
			.tag(CBCTags.ItemCBC.BLOCK_CAST_IRON)
			.build()
			.register();
	
	public static final BlockEntry<Block> NETHERSTEEL_BLOCK = REGISTRATE
			.block("nethersteel_block", Block::new)
			.initialProperties(() -> Blocks.NETHERITE_BLOCK)
			.lang("Block of Nethersteel")
			.simpleItem()
			.register();
	
	//////// Projectiles ////////
	
	public static final BlockEntry<SolidShotBlock> SOLID_SHOT = REGISTRATE
			.block("solid_shot", SolidShotBlock::new)
			.initialProperties(Material.METAL)
			.properties(p -> p.strength(2.0f, 3.0f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/solid_shot"))
			.simpleItem()
			.register();
	
	public static final BlockEntry<HEShellBlock> HE_SHELL = REGISTRATE
			.block("he_shell", HEShellBlock::new)
			.transform(shell(MaterialColor.COLOR_RED))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/he_shell"))
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("High Explosive (HE) Shell")
			.simpleItem()
			.register();
	
	public static final BlockEntry<ShrapnelShellBlock> SHRAPNEL_SHELL = REGISTRATE
			.block("shrapnel_shell", ShrapnelShellBlock::new)
			.transform(shell(MaterialColor.COLOR_GREEN))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shrapnel_shell"))
			.loot(CBCBuilderTransformers.shellLoot())
			.simpleItem()
			.register();
	
	public static final BlockEntry<APShellBlock> AP_SHELL = REGISTRATE
			.block("ap_shell", APShellBlock::new)
			.transform(shell(MaterialColor.COLOR_BLUE))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/ap_shell"))
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Armor Piercing (AP) Shell")
			.simpleItem()
			.register();
	
	public static final BlockEntry<FluidShellBlock> FLUID_SHELL = REGISTRATE
			.block("fluid_shell", FluidShellBlock::new)
			.transform(shell(MaterialColor.COLOR_ORANGE))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/fluid_shell"))
			.loot(CBCBuilderTransformers.shellLoot(f -> f.copy("FluidContent", "BlockEntityTag.FluidContent")))
			.simpleItem()
			.register();
	
	public static final BlockEntry<GrapeshotBlock> BAG_OF_GRAPESHOT = REGISTRATE
			.block("bag_of_grapeshot", GrapeshotBlock::new)
			.initialProperties(Material.METAL, MaterialColor.WOOL)
			.properties(p -> p.sound(SoundType.WOOL))
			.transform(CBCBuilderTransformers.projectile("projectile/grapeshot"))
			.lang("Bag of Grapeshot")
			.simpleItem()
			.register();

	public static final BlockEntry<MortarStoneBlock> MORTAR_STONE = REGISTRATE
			.block("mortar_stone", MortarStoneBlock::new)
			.initialProperties(SharedProperties::stone)
			.transform(CBCBuilderTransformers.projectile("projectile/mortar_stone"))
			.item(MortarStoneItem::new).build()
			.register();
	
	public static final BlockEntry<PowderChargeBlock> POWDER_CHARGE = REGISTRATE
			.block("powder_charge", PowderChargeBlock::new)
			.initialProperties(() -> Blocks.TNT)
			.properties(p -> p.sound(SoundType.WOOL))
			.transform(CBCBuilderTransformers.powderCharge())
			.simpleItem()
			.register();
	
	//////// Crafting blocks ////////
	
	public static final BlockEntry<Block> CASTING_SAND = REGISTRATE
			.block("casting_sand", Block::new)
			.transform(castingSand())
			.simpleItem()
			.register();
	
	public static final BlockEntry<CannonCastBlock> CANNON_CAST = REGISTRATE
			.block("cannon_cast", CannonCastBlock::new)
			.transform(castingSand())
			.properties(p -> p.noOcclusion())
			.transform(CBCBuilderTransformers.invisibleWithParticle("block/casting_sand"))
			.register();
	
	public static final BlockEntry<FinishedCannonCastBlock> FINISHED_CANNON_CAST = REGISTRATE
			.block("finished_cannon_cast", FinishedCannonCastBlock::new)
			.transform(castingSand())
			.properties(p -> p.noOcclusion())
			.transform(CBCBuilderTransformers.invisibleWithParticle("block/casting_sand"))
			.register();
	
	public static final BlockEntry<CannonCastMouldBlock> VERY_SMALL_CAST_MOULD =
			castMould("very_small", Block.box(2, 0, 2, 14, 17, 14), CannonCastShape.VERY_SMALL);
	
	public static final BlockEntry<CannonCastMouldBlock> SMALL_CAST_MOULD =
			castMould("small", Block.box(1, 0, 1, 15, 17, 15), CannonCastShape.SMALL);
	
	public static final BlockEntry<CannonCastMouldBlock> MEDIUM_CAST_MOULD =
			castMould("medium", Block.box(0, 0, 0, 16, 17, 16), CannonCastShape.MEDIUM);
	
	public static final BlockEntry<CannonCastMouldBlock> LARGE_CAST_MOULD =
			castMould("large", Block.box(-1, 0, -1, 17, 17, 17), CannonCastShape.LARGE);
	
	public static final BlockEntry<CannonCastMouldBlock> VERY_LARGE_CAST_MOULD =
			castMould("very_large", Block.box(-2, 0, -2, 18, 17, 18), CannonCastShape.VERY_LARGE);
	
	public static final BlockEntry<CannonCastMouldBlock> CANNON_END_CAST_MOULD =
			castMould("cannon_end", Shapes.or(Block.box(5, 0, 5, 11, 6, 11), Block.box(6, 6, 6, 10, 8, 10), Block.box(0, 8, 0, 16, 16, 16)), CannonCastShape.CANNON_END);
	
	public static final BlockEntry<CannonCastMouldBlock> SLIDING_BREECH_CAST_MOULD =
			castMould("sliding_breech", Block.box(0, 0, 0, 16, 17, 16), CannonCastShape.SLIDING_BREECH);
	
	public static final BlockEntry<CannonCastMouldBlock> SCREW_BREECH_CAST_MOULD =
			castMould("screw_breech", Block.box(0, 8, 0, 16, 17, 16), CannonCastShape.SCREW_BREECH);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_BREECH_CAST_MOULD =
			castMould("autocannon_breech", Block.box(4, 0, 4, 12, 17, 12), CannonCastShape.AUTOCANNON_BREECH);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_RECOIL_SPRING_CAST_MOULD =
			castMould("autocannon_recoil_spring", Block.box(5, 0, 5, 11, 17, 11), CannonCastShape.AUTOCANNON_RECOIL_SPRING);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_BARREL_CAST_MOULD =
			castMould("autocannon_barrel", Block.box(6, 0, 6, 10, 17, 10), CannonCastShape.AUTOCANNON_BARREL);

	public static final BlockEntry<BasinFoundryLidBlock> BASIN_FOUNDRY_LID = REGISTRATE
			.block("basin_foundry_lid", BasinFoundryLidBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.color(MaterialColor.COLOR_GRAY))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.properties(p -> p.noOcclusion())
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.blockstate((b, p) -> p.simpleBlock(b.get(), p.models().getExistingFile(CreateBigCannons.resource("block/basin_foundry_lid"))))
			.simpleItem()
			.register();
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> cannonBlock() {
		return cannonBlock(true);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> cannonBlock(boolean canPassThrough) {
		NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> transform = b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(5.0f, 6.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_IRON_TOOL);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH)) : transform;
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> logCannonBlock() {
		return logCannonBlock(true);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> logCannonBlock(boolean canPassThrough) {
		NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> transform = b -> b.initialProperties(Material.WOOD, MaterialColor.CRIMSON_STEM)
				.properties(p -> p.strength(2.0f))
				.properties(p -> p.sound(SoundType.WOOD))
				.tag(BlockTags.MINEABLE_WITH_AXE)
				.tag(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH)) : transform;
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> strongCannonBlock() {
		return strongCannonBlock(true);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> strongCannonBlock(boolean canPassThrough) {
		NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> transform = b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(50.0f, 1200.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_DIAMOND_TOOL)
				.tag(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH)) : transform;
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> shell(MaterialColor color) {
		return b -> b.initialProperties(Material.EXPLOSIVE, color)
				.properties(p -> p.strength(2.0f, 3.0f))
				.properties(p -> p.sound(SoundType.STONE));
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> castingSand() {
		return b -> b.initialProperties(() -> Blocks.SAND)
				.tag(BlockTags.MINEABLE_WITH_SHOVEL);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
		return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE);
	}
	
	private static BlockEntry<CannonCastMouldBlock> castMould(String name, VoxelShape blockShape, Supplier<CannonCastShape> castShape) {
		return REGISTRATE.block(name + "_cast_mould", p -> new CannonCastMouldBlock(p, blockShape, castShape))
				.transform(CBCBuilderTransformers.castMould(name))
				.register();
	}
	
	private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }
	
	public static void register() {}
	
}
