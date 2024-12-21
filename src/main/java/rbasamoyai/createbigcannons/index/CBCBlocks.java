package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Supplier;

import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.ModGroup;
import rbasamoyai.createbigcannons.base.CBCDefaultStress;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlock;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountDisplaySource;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountExtensionBlock;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlock;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlock;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannon_loading.RamHeadBlock;
import rbasamoyai.createbigcannons.cannon_loading.WormHeadBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockItem;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonTubeBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BuiltUpCannonCTBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechCTBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.drop_mortar.DropMortarEndBlock;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.DrillBitBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredAutocannonBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredBigCannonBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredScrewBreechBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredSlidingBreechBlock;
import rbasamoyai.createbigcannons.crafting.builtup.BigCannonLayerBlock;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpCannonBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderHeadBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlock;
import rbasamoyai.createbigcannons.crafting.foundry.BasinFoundryLidBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteSlidingBreechBlock;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell.APShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.he_shell.HEShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PowderChargeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.PowderChargeItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone.TrafficConeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone.TrafficConeBlockItem;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

public class CBCBlocks {

	static { ModGroup.useModTab(ModGroup.MAIN_TAB_KEY); }

	//////// Log cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> LOG_CANNON_CHAMBER = REGISTRATE
		.block("log_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.LOG))
		.transform(logCannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("log", true))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonEndBlock> LOG_CANNON_END = REGISTRATE
		.block("log_cannon_end", p -> new BigCannonEndBlock(p, CBCBigCannonMaterials.LOG))
		.transform(logCannonBlock())
		.transform(CBCBuilderTransformers.cannonEnd("cannon_end/log"))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Wrought Iron cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> WROUGHT_IRON_CANNON_CHAMBER = REGISTRATE
		.block("wrought_iron_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.WROUGHT_IRON))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("wrought_iron", true))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonEndBlock> WROUGHT_IRON_CANNON_END = REGISTRATE
		.block("wrought_iron_cannon_end", p -> new BigCannonEndBlock(p, CBCBigCannonMaterials.WROUGHT_IRON))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonEnd("cannon_end/wrought_iron"))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<DropMortarEndBlock> WROUGHT_IRON_DROP_MORTAR_END = REGISTRATE
		.block("wrought_iron_drop_mortar_end", p -> new DropMortarEndBlock(p, CBCBigCannonMaterials.WROUGHT_IRON))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.dropMortarEnd("cannon_end/wrought_iron"))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Cast Iron cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> CAST_IRON_CANNON_BARREL = REGISTRATE
		.block("cast_iron_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, CBCBigCannonMaterials.CAST_IRON))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.cannonBarrel("cast_iron", true))
		.loot(CBCBuilderTransformers.castIronScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> CAST_IRON_CANNON_CHAMBER = REGISTRATE
		.block("cast_iron_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.CAST_IRON))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("cast_iron", true))
		.loot(CBCBuilderTransformers.castIronScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonEndBlock> CAST_IRON_CANNON_END = REGISTRATE
		.block("cast_iron_cannon_end", p -> new BigCannonEndBlock(p, CBCBigCannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonEnd("cannon_end/cast_iron"))
		.loot(CBCBuilderTransformers.castIronScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<QuickfiringBreechBlock> CAST_IRON_QUICKFIRING_BREECH = REGISTRATE
		.block("cast_iron_quickfiring_breech", p -> new QuickfiringBreechBlock(p, CBCBigCannonMaterials.CAST_IRON, castIronSlidingBreech()))
		.lang("Cast Iron Quick-Firing Breech")
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/cast_iron"))
		.loot(CBCBuilderTransformers.castIronScrapLoot(10))
		.register();

	private static NonNullSupplier<? extends Block> castIronSlidingBreech() {
		return CAST_IRON_SLIDING_BREECH;
	}

	public static final BlockEntry<SlidingBreechBlock> CAST_IRON_SLIDING_BREECH = REGISTRATE
		.block("cast_iron_sliding_breech", p -> new SlidingBreechBlock(p, CBCBigCannonMaterials.CAST_IRON, CAST_IRON_QUICKFIRING_BREECH))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/cast_iron"))
		.loot(CBCBuilderTransformers.castIronScrapLoot(10))
		.transform(CBCDefaultStress.setImpact(16.0d))
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_CAST_IRON_CANNON_BARREL = REGISTRATE
		.block("unbored_cast_iron_cannon_barrel", p -> UnboredBigCannonBlock.verySmall(p, CBCBigCannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonBarrel("cast_iron", false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_CAST_IRON_CANNON_CHAMBER = REGISTRATE
		.block("unbored_cast_iron_cannon_chamber", p -> UnboredBigCannonBlock.medium(p, CBCBigCannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonChamber("cast_iron", false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_CAST_IRON_SLIDING_BREECH = REGISTRATE
		.block("incomplete_cast_iron_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, CBCBigCannonMaterials.CAST_IRON, CBCItems.CAST_IRON_SLIDING_BREECHBLOCK, CAST_IRON_SLIDING_BREECH))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/cast_iron"))
		.loot(CBCBuilderTransformers.castIronScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_CAST_IRON_SLIDING_BREECH = REGISTRATE
		.block("unbored_cast_iron_sliding_breech", p -> new UnboredSlidingBreechBlock(p, CBCBigCannonMaterials.CAST_IRON, Shapes.block()))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_cast_iron"))
		.loot(CBCBuilderTransformers.castIronScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Bronze cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> BRONZE_CANNON_BARREL = REGISTRATE
		.block("bronze_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, CBCBigCannonMaterials.BRONZE))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.cannonBarrel("bronze", true))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> BRONZE_CANNON_CHAMBER = REGISTRATE
		.block("bronze_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.BRONZE))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("bronze", true))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonEndBlock> BRONZE_CANNON_END = REGISTRATE
		.block("bronze_cannon_end", p -> new BigCannonEndBlock(p, CBCBigCannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonEnd("cannon_end/bronze"))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<QuickfiringBreechBlock> BRONZE_QUICKFIRING_BREECH = REGISTRATE
		.block("bronze_quickfiring_breech", p -> new QuickfiringBreechBlock(p, CBCBigCannonMaterials.BRONZE, bronzeSlidingBreech()))
		.lang("Bronze Quick-Firing Breech")
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/bronze"))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.register();

	private static NonNullSupplier<? extends Block> bronzeSlidingBreech() {
		return BRONZE_SLIDING_BREECH;
	}

	public static final BlockEntry<SlidingBreechBlock> BRONZE_SLIDING_BREECH = REGISTRATE
		.block("bronze_sliding_breech", p -> new SlidingBreechBlock(p, CBCBigCannonMaterials.BRONZE, BRONZE_QUICKFIRING_BREECH))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/bronze"))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.transform(CBCDefaultStress.setImpact(12.0d))
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_BRONZE_CANNON_BARREL = REGISTRATE
		.block("unbored_bronze_cannon_barrel", p -> UnboredBigCannonBlock.verySmall(p, CBCBigCannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonBarrel("bronze", false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_BRONZE_CANNON_CHAMBER = REGISTRATE
		.block("unbored_bronze_cannon_chamber", p -> UnboredBigCannonBlock.medium(p, CBCBigCannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.cannonChamber("bronze", false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_BRONZE_SLIDING_BREECH = REGISTRATE
		.block("incomplete_bronze_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, CBCBigCannonMaterials.BRONZE, CBCItems.BRONZE_SLIDING_BREECHBLOCK, BRONZE_SLIDING_BREECH))
		.transform(cannonBlock())
		.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/bronze"))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(10))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_BRONZE_SLIDING_BREECH = REGISTRATE
		.block("unbored_bronze_sliding_breech", p -> new UnboredSlidingBreechBlock(p, CBCBigCannonMaterials.BRONZE, Shapes.block()))
		.transform(cannonBlock(false))
		.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_bronze"))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Steel cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> STEEL_CANNON_BARREL = REGISTRATE
		.block("steel_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.cannonBarrel("steel", true))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.STEEL_CANNON_BARREL)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_STEEL_CANNON_BARREL = REGISTRATE
		.block("built_up_steel_cannon_barrel", p -> BigCannonTubeBlock.small(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.builtUpCannonBarrel("steel", true))
		.lang("Built-Up Steel Cannon Barrel")
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.BUILT_UP_STEEL_CANNON_BARREL)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> STEEL_CANNON_CHAMBER = REGISTRATE
		.block("steel_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("steel", true))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.STEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_STEEL_CANNON_CHAMBER = REGISTRATE
		.block("built_up_steel_cannon_chamber", p -> BigCannonTubeBlock.large(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.builtUpCannonChamber("steel", true))
		.lang("Built-Up Steel Cannon Chamber")
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.BUILT_UP_STEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> THICK_STEEL_CANNON_CHAMBER = REGISTRATE
		.block("thick_steel_cannon_chamber", p -> BigCannonTubeBlock.veryLarge(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.thickCannonChamber("steel", true))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.THICK_STEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> VERY_SMALL_STEEL_CANNON_LAYER = REGISTRATE
		.block("very_small_steel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.STEEL, () -> CannonCastShape.VERY_SMALL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("very_small", "cannon_tubing/steel"))
		.loot(CBCBuilderTransformers.steelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> SMALL_STEEL_CANNON_LAYER = REGISTRATE
		.block("small_steel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.STEEL, () -> CannonCastShape.SMALL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("small", "cannon_tubing/steel"))
		.loot(CBCBuilderTransformers.steelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> MEDIUM_STEEL_CANNON_LAYER = REGISTRATE
		.block("medium_steel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.STEEL, () -> CannonCastShape.MEDIUM))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("medium", "cannon_tubing/steel"))
		.loot(CBCBuilderTransformers.steelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> LARGE_STEEL_CANNON_LAYER = REGISTRATE
		.block("large_steel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.STEEL, () -> CannonCastShape.LARGE))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("large", "cannon_tubing/steel"))
		.loot(CBCBuilderTransformers.steelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> VERY_LARGE_STEEL_CANNON_LAYER = REGISTRATE
		.block("very_large_steel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.STEEL, () -> CannonCastShape.VERY_LARGE))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("very_large", "cannon_tubing/steel"))
		.loot(CBCBuilderTransformers.steelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_SMALL_STEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_very_small_steel_cannon_layer", p -> UnboredBigCannonBlock.verySmall(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.cannonBarrel("steel", false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_SMALL_STEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_small_steel_cannon_layer", p -> UnboredBigCannonBlock.small(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.builtUpCannonBarrel("steel", false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_MEDIUM_STEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_medium_steel_cannon_layer", p -> UnboredBigCannonBlock.medium(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.cannonChamber("steel", false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_LARGE_STEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_large_steel_cannon_layer", p -> UnboredBigCannonBlock.large(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.builtUpCannonChamber("steel", false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_LARGE_STEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_very_large_steel_cannon_layer", p -> UnboredBigCannonBlock.veryLarge(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.thickCannonChamber("steel", false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<QuickfiringBreechBlock> STEEL_QUICKFIRING_BREECH = REGISTRATE
		.block("steel_quickfiring_breech", p -> new QuickfiringBreechBlock(p, CBCBigCannonMaterials.STEEL, steelSlidingBreech()))
		.lang("Steel Quick-Firing Breech")
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/steel"))
		.onRegister(CreateRegistrate.connectedTextures(() ->
			new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
		.register();

	private static NonNullSupplier<? extends Block> steelSlidingBreech() {
		return STEEL_SLIDING_BREECH;
	}

	public static final BlockEntry<SlidingBreechBlock> STEEL_SLIDING_BREECH = REGISTRATE
		.block("steel_sliding_breech", p -> new SlidingBreechBlock(p, CBCBigCannonMaterials.STEEL, STEEL_QUICKFIRING_BREECH))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/steel"))
		.transform(CBCDefaultStress.setImpact(32.0d))
		.onRegister(CreateRegistrate.connectedTextures(() ->
			new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
		.register();

	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_STEEL_SLIDING_BREECH = REGISTRATE
		.block("incomplete_steel_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, CBCBigCannonMaterials.STEEL, CBCItems.STEEL_SLIDING_BREECHBLOCK, STEEL_SLIDING_BREECH))
		.transform(strongCannonBlock())
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/steel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_STEEL_SLIDING_BREECH = REGISTRATE
		.block("unbored_steel_sliding_breech", p -> new UnboredSlidingBreechBlock(p, CBCBigCannonMaterials.STEEL, Shapes.block()))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_steel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<ScrewBreechBlock> STEEL_SCREW_BREECH = REGISTRATE
		.block("steel_screw_breech", p -> new ScrewBreechBlock(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.transform(CBCBuilderTransformers.screwBreech("screw_breech/steel"))
		.transform(CBCDefaultStress.setImpact(16.0d))
		.register();

	public static final BlockEntry<IncompleteScrewBreechBlock> INCOMPLETE_STEEL_SCREW_BREECH = REGISTRATE
		.block("incomplete_steel_screw_breech", p -> new IncompleteScrewBreechBlock(p, CBCBigCannonMaterials.STEEL, CBCItems.STEEL_SCREW_LOCK, STEEL_SCREW_BREECH))
		.transform(strongCannonBlock())
		.loot(CBCBuilderTransformers.steelScrapLoot(10))
		.transform(CBCBuilderTransformers.screwBreechIncomplete("screw_breech/steel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredScrewBreechBlock> UNBORED_STEEL_SCREW_BREECH = REGISTRATE
		.block("unbored_steel_screw_breech", p -> new UnboredScrewBreechBlock(p, CBCBigCannonMaterials.STEEL))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(15))
		.transform(CBCBuilderTransformers.screwBreechUnbored("screw_breech/steel", "screw_breech/unbored_steel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Nethersteel cannon blocks ////////

	public static final BlockEntry<BigCannonTubeBlock> NETHERSTEEL_CANNON_BARREL = REGISTRATE
		.block("nethersteel_cannon_barrel", p -> BigCannonTubeBlock.verySmall(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.cannonBarrel("nethersteel", true))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.NETHERSTEEL_CANNON_BARREL)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_NETHERSTEEL_CANNON_BARREL = REGISTRATE
		.block("built_up_nethersteel_cannon_barrel", p -> BigCannonTubeBlock.small(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.builtUpCannonBarrel("nethersteel", true))
		.lang("Built-Up Nethersteel Cannon Barrel")
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.BUILT_UP_NETHERSTEEL_CANNON_BARREL)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
		.block("nethersteel_cannon_chamber", p -> BigCannonTubeBlock.medium(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.cannonChamber("nethersteel", true))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.NETHERSTEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> BUILT_UP_NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
		.block("built_up_nethersteel_cannon_chamber", p -> BigCannonTubeBlock.large(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.builtUpCannonChamber("nethersteel", true))
		.lang("Built-Up Nethersteel Cannon Chamber")
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonTubeBlock> THICK_NETHERSTEEL_CANNON_CHAMBER = REGISTRATE
		.block("thick_nethersteel_cannon_chamber", p -> BigCannonTubeBlock.veryLarge(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.thickCannonChamber("nethersteel", true))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.onRegister(CreateRegistrate.connectedTextures(() -> new BuiltUpCannonCTBehavior(CBCSpriteShifts.THICK_NETHERSTEEL_CANNON_CHAMBER)))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> VERY_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("very_small_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, () -> CannonCastShape.VERY_SMALL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("very_small", "cannon_tubing/nethersteel"))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("small_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, () -> CannonCastShape.SMALL))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("small", "cannon_tubing/nethersteel"))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> MEDIUM_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("medium_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, () -> CannonCastShape.MEDIUM))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("medium", "cannon_tubing/nethersteel"))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("large_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, () -> CannonCastShape.LARGE))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("large", "cannon_tubing/nethersteel"))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<BigCannonLayerBlock> VERY_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("very_large_nethersteel_cannon_layer", p -> new BigCannonLayerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, () -> CannonCastShape.VERY_LARGE))
		.transform(strongCannonBlock())
		.transform(CBCBuilderTransformers.sizedHollowCannon("very_large", "cannon_tubing/nethersteel"))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(5))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_very_small_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.verySmall(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.cannonBarrel("nethersteel", false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_small_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.small(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.builtUpCannonBarrel("nethersteel", false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_medium_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.medium(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.cannonChamber("nethersteel", false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_large_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.large(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.builtUpCannonChamber("nethersteel", false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredBigCannonBlock> UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER = REGISTRATE
		.block("unbored_very_large_nethersteel_cannon_layer", p -> UnboredBigCannonBlock.veryLarge(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.transform(CBCBuilderTransformers.thickCannonChamber("nethersteel", false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<ScrewBreechBlock> NETHERSTEEL_SCREW_BREECH = REGISTRATE
		.block("nethersteel_screw_breech", p -> new ScrewBreechBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.transform(CBCBuilderTransformers.screwBreech("screw_breech/nethersteel"))
		.transform(CBCDefaultStress.setImpact(40.0d))
		.register();

	public static final BlockEntry<IncompleteScrewBreechBlock> INCOMPLETE_NETHERSTEEL_SCREW_BREECH = REGISTRATE
		.block("incomplete_nethersteel_screw_breech", p -> new IncompleteScrewBreechBlock(p, CBCBigCannonMaterials.NETHERSTEEL, CBCItems.NETHERSTEEL_SCREW_LOCK, NETHERSTEEL_SCREW_BREECH))
		.transform(strongCannonBlock())
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(10))
		.transform(CBCBuilderTransformers.screwBreechIncomplete("screw_breech/nethersteel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	public static final BlockEntry<UnboredScrewBreechBlock> UNBORED_NETHERSTEEL_SCREW_BREECH = REGISTRATE
		.block("unbored_nethersteel_screw_breech", p -> new UnboredScrewBreechBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
		.transform(strongCannonBlock(false))
		.loot(CBCBuilderTransformers.nethersteelScrapLoot(15))
		.transform(CBCBuilderTransformers.screwBreechUnbored("screw_breech/nethersteel", "screw_breech/unbored_nethersteel"))
		.item(BigCannonBlockItem::new).build()
		.register();

	//////// Cast iron autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> CAST_IRON_AUTOCANNON_BARREL = REGISTRATE
		.block("cast_iron_autocannon_barrel", p -> new AutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.castIronScrapLoot(2))
		.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/cast_iron"))
		.register();

	public static final BlockEntry<AutocannonBreechBlock> CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
		.block("cast_iron_autocannon_breech", p -> new AutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/cast_iron", true))
		.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("cast_iron_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, CBCBlocks::castIronAutocannonBarrel))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/cast_iron", true))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("incomplete_cast_iron_autocannon_recoil_spring", p ->
			IncompleteAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.CAST_IRON, CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.castIronScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/cast_iron", false))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
		.block("incomplete_cast_iron_autocannon_breech", p ->
			IncompleteAutocannonBlock.breech(p, CBCAutocannonMaterials.CAST_IRON, CAST_IRON_AUTOCANNON_BREECH, CBCItems.CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.castIronScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/cast_iron", false))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_BARREL = REGISTRATE
		.block("unbored_cast_iron_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, CBCAutocannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(4))
		.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/cast_iron"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("unbored_cast_iron_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(6))
		.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/cast_iron"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_CAST_IRON_AUTOCANNON_BREECH = REGISTRATE
		.block("unbored_cast_iron_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, CBCAutocannonMaterials.CAST_IRON))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.castIronScrapLoot(8))
		.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/cast_iron"))
		.register();

	//////// Bronze autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> BRONZE_AUTOCANNON_BARREL = REGISTRATE
		.block("bronze_autocannon_barrel", p -> new AutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.bronzeScrapLoot(2))
		.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/bronze"))
		.register();

	public static final BlockEntry<AutocannonBreechBlock> BRONZE_AUTOCANNON_BREECH = REGISTRATE
		.block("bronze_autocannon_breech", p -> new AutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/bronze", true))
		.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("bronze_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, CBCBlocks::bronzeAutocannonBarrel))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/bronze", true))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("incomplete_bronze_autocannon_recoil_spring", p ->
			IncompleteAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.BRONZE, BRONZE_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.bronzeScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/bronze", false))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_BRONZE_AUTOCANNON_BREECH = REGISTRATE
		.block("incomplete_bronze_autocannon_breech", p ->
			IncompleteAutocannonBlock.breech(p, CBCAutocannonMaterials.BRONZE, BRONZE_AUTOCANNON_BREECH, CBCItems.BRONZE_AUTOCANNON_BREECH_EXTRACTOR))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/bronze", false))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_BARREL = REGISTRATE
		.block("unbored_bronze_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, CBCAutocannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(4))
		.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/bronze"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("unbored_bronze_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(6))
		.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/bronze"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_BRONZE_AUTOCANNON_BREECH = REGISTRATE
		.block("unbored_bronze_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, CBCAutocannonMaterials.BRONZE))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.bronzeScrapLoot(8))
		.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/bronze"))
		.register();

	//////// Steel autocannon blocks ////////

	public static final BlockEntry<AutocannonBarrelBlock> STEEL_AUTOCANNON_BARREL = REGISTRATE
		.block("steel_autocannon_barrel", p -> new AutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.steelScrapLoot(2))
		.transform(CBCBuilderTransformers.autocannonBarrel("autocannon/steel"))
		.register();

	public static final BlockEntry<AutocannonBreechBlock> STEEL_AUTOCANNON_BREECH = REGISTRATE
		.block("steel_autocannon_breech", p -> new AutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/steel", true))
		.register();

	public static final BlockEntry<AutocannonRecoilSpringBlock> STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("steel_autocannon_recoil_spring", p -> new AutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, CBCBlocks::steelAutocannonBarrel))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/steel", true))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("incomplete_steel_autocannon_recoil_spring", p ->
			IncompleteAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.STEEL, STEEL_AUTOCANNON_RECOIL_SPRING, CBCItems.RECOIL_SPRING))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.steelScrapLoot(3))
		.transform(CBCBuilderTransformers.autocannonRecoilSpring("autocannon/steel", false))
		.register();

	public static final BlockEntry<IncompleteAutocannonBlock> INCOMPLETE_STEEL_AUTOCANNON_BREECH = REGISTRATE
		.block("incomplete_steel_autocannon_breech", p ->
			IncompleteAutocannonBlock.breech(p, CBCAutocannonMaterials.STEEL, STEEL_AUTOCANNON_BREECH, CBCItems.STEEL_AUTOCANNON_BREECH_EXTRACTOR))
		.transform(cannonBlock())
		.loot(CBCBuilderTransformers.steelScrapLoot(4))
		.transform(CBCBuilderTransformers.autocannonBreech("autocannon/steel", false))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_BARREL = REGISTRATE
		.block("unbored_steel_autocannon_barrel", p -> UnboredAutocannonBlock.barrel(p, CBCAutocannonMaterials.STEEL))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(4))
		.transform(CBCBuilderTransformers.unboredAutocannonBarrel("autocannon/steel"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.block("unbored_steel_autocannon_recoil_spring", p -> UnboredAutocannonBlock.recoilSpring(p, CBCAutocannonMaterials.STEEL))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(6))
		.transform(CBCBuilderTransformers.unboredAutocannonRecoilSpring("autocannon/steel"))
		.register();

	public static final BlockEntry<UnboredAutocannonBlock> UNBORED_STEEL_AUTOCANNON_BREECH = REGISTRATE
		.block("unbored_steel_autocannon_breech", p -> UnboredAutocannonBlock.breech(p, CBCAutocannonMaterials.STEEL))
		.transform(cannonBlock(false))
		.loot(CBCBuilderTransformers.steelScrapLoot(8))
		.transform(CBCBuilderTransformers.unboredAutocannonBreech("autocannon/steel"))
		.register();

	//////// Cannon loading blocks ////////

	public static final BlockEntry<CannonLoaderBlock> CANNON_LOADER = REGISTRATE
		.block("cannon_loader", CannonLoaderBlock::new)
		.transform(cbcMachine())
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonLoader())
		.transform(CBCDefaultStress.setImpact(4.0d))
		.register();

	public static final BlockEntry<RamHeadBlock> RAM_HEAD = REGISTRATE
		.block("ram_head", RamHeadBlock::new)
		.initialProperties(() -> Blocks.PISTON_HEAD)
		.properties(p -> p.pushReaction(PushReaction.NORMAL))
		.properties(p -> p.sound(SoundType.WOOD))
		.transform(CBCBuilderTransformers.ramHead())
		.transform(axeOrPickaxe())
		.simpleItem()
		.register();

	public static final BlockEntry<WormHeadBlock> WORM_HEAD = REGISTRATE
		.block("worm_head", WormHeadBlock::new)
		.initialProperties(() -> Blocks.PISTON_HEAD)
		.properties(p -> p.pushReaction(PushReaction.NORMAL))
		.properties(p -> p.sound(SoundType.METAL))
		.transform(CBCBuilderTransformers.wormHead())
		.transform(axeOrPickaxe())
		.simpleItem()
		.register();

	//////// Cannon mount blocks ////////

	public static final BlockEntry<CannonMountBlock> CANNON_MOUNT = REGISTRATE
		.block("cannon_mount", CannonMountBlock::new)
		.transform(cbcMachine())
		.properties(p -> p.isRedstoneConductor(CBCBlocks::never))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonMount())
		.onRegister(AllDisplayBehaviours.assignDataBehaviour(new CannonMountDisplaySource()))
		.register();

	public static final BlockEntry<YawControllerBlock> YAW_CONTROLLER = REGISTRATE
		.block("yaw_controller", YawControllerBlock::new)
		.transform(cbcMachine())
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.yawController())
		.onRegister(AllDisplayBehaviours.assignDataBehaviour(new CannonMountDisplaySource()))
		.register();

	public static final BlockEntry<CannonMountExtensionBlock> CANNON_MOUNT_EXTENSION = REGISTRATE
		.block("cannon_mount_extension", CannonMountExtensionBlock::new)
		.transform(cbcMachine())
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonMountExtension())
		.onRegister(AllDisplayBehaviours.assignDataBehaviour(new CannonMountDisplaySource()))
		.register();

	public static final BlockEntry<CannonCarriageBlock> CANNON_CARRIAGE = REGISTRATE
		.block("cannon_carriage", CannonCarriageBlock::new)
		.initialProperties(() -> Blocks.OAK_PLANKS)
		.properties(p -> p.mapColor(MapColor.PODZOL))
		.transform(axeOnly())
		.properties(p -> p.isRedstoneConductor(CBCBlocks::never))
		.transform(CBCBuilderTransformers.cannonCarriage())
		.register();

	//////// Cannon crafting mechanism blocks ////////

	public static final BlockEntry<CannonDrillBlock> CANNON_DRILL = REGISTRATE
		.block("cannon_drill", CannonDrillBlock::new)
		.transform(cbcMachine())
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonDrill())
		.transform(CBCDefaultStress.setImpact(8.0d))
		.register();

	public static final BlockEntry<DrillBitBlock> CANNON_DRILL_BIT = REGISTRATE
		.block("cannon_drill_bit", DrillBitBlock::new)
		.initialProperties(() -> Blocks.PISTON_HEAD)
		.properties(p -> p.pushReaction(PushReaction.NORMAL))
		.properties(p -> p.mapColor(MapColor.STONE))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonDrillBit())
		.register();

	public static final BlockEntry<CannonBuilderBlock> CANNON_BUILDER = REGISTRATE
		.block("cannon_builder", CannonBuilderBlock::new)
		.transform(cbcMachine())
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonBuilder())
		.transform(CBCDefaultStress.setImpact(8.0d))
		.register();

	public static final BlockEntry<CannonBuilderHeadBlock> CANNON_BUILDER_HEAD = REGISTRATE
		.block("cannon_builder_head", CannonBuilderHeadBlock::new)
		.initialProperties(() -> Blocks.PISTON_HEAD)
		.properties(p -> p.mapColor(MapColor.STONE))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.cannonBuilderHead())
		.register();

	public static final BlockEntry<BuiltUpCannonBlock> BUILT_UP_CANNON = REGISTRATE
		.block("built_up_cannon", BuiltUpCannonBlock::new)
		.transform(strongCannonBlock())
		.properties(p -> p.noOcclusion())
		.blockstate(CBCBuilderTransformers.builtUpCannon())
		.register();

	public static final BlockEntry<Block> CAST_IRON_BLOCK = REGISTRATE
		.block("cast_iron_block", Block::new)
		.properties(p -> p.mapColor(MapColor.METAL))
		.properties(p -> p.requiresCorrectToolForDrops())
		.properties(p -> p.strength(5.0F, 6.0F))
		.properties(p -> p.sound(SoundType.METAL))
		.tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.tag(BlockTags.NEEDS_STONE_TOOL)
		.lang("Block of Cast Iron")
		.item()
		.tag(CBCTags.CBCItemTags.BLOCK_CAST_IRON)
		.build()
		.register();

	public static final BlockEntry<Block> NETHERSTEEL_BLOCK = REGISTRATE
		.block("nethersteel_block", Block::new)
		.initialProperties(() -> Blocks.NETHERITE_BLOCK)
		.properties(p -> p.requiresCorrectToolForDrops())
		.properties(p -> p.strength(50.0F, 1200.0F))
		.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
		.tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.tag(BlockTags.NEEDS_DIAMOND_TOOL)
		.lang("Block of Nethersteel")
		.item()
		.tag(CBCTags.CBCItemTags.BLOCK_NETHERSTEEL)
		.build()
		.register();

	//////// Projectiles ////////

	public static final BlockEntry<SolidShotBlock> SOLID_SHOT = REGISTRATE
		.block("solid_shot", SolidShotBlock::new)
		.properties(p -> p.strength(2.0f, 3.0f))
		.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/solid_shot"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.tracerProjectileLoot())
		.item(ProjectileBlockItem::new)
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<HEShellBlock> HE_SHELL = REGISTRATE
		.block("he_shell", HEShellBlock::new)
		.transform(shell(MapColor.COLOR_RED))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/he_shell"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot())
		.lang("High Explosive (HE) Shell")
		.item(FuzedProjectileBlockItem::new)
		.transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/he_shell"))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<ShrapnelShellBlock> SHRAPNEL_SHELL = REGISTRATE
		.block("shrapnel_shell", ShrapnelShellBlock::new)
		.transform(shell(MapColor.COLOR_GREEN))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/shrapnel_shell"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot())
		.item(FuzedProjectileBlockItem::new)
		.transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/shrapnel_shell"))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<APShotBlock> AP_SHOT = REGISTRATE
		.block("ap_shot", APShotBlock::new)
		.transform(shell(MapColor.COLOR_LIGHT_GRAY))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/ap_shot"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.tracerProjectileLoot())
		.lang("Armor Piercing (AP) Shot")
		.item(ProjectileBlockItem::new)
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<TrafficConeBlock> TRAFFIC_CONE = REGISTRATE
		.block("traffic_cone", TrafficConeBlock::new)
		.addLayer(() -> RenderType::solid)
		.properties(p -> p.mapColor(MapColor.COLOR_ORANGE))
		.properties(p -> p.instabreak())
		.properties(p -> p.sound(SoundType.WOOD))
		.properties(p -> p.noOcclusion())
		.blockstate(CBCBuilderTransformers.trafficCone())
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.tracerProjectileLoot())
		.item(TrafficConeBlockItem::new)
		.initialProperties(() -> new Item.Properties().rarity(Rarity.EPIC))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<APShellBlock> AP_SHELL = REGISTRATE
		.block("ap_shell", APShellBlock::new)
		.transform(shell(MapColor.COLOR_BLUE))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/ap_shell"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot())
		.lang("Armor Piercing (AP) Shell")
		.item(FuzedProjectileBlockItem::new)
		.transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/ap_shell"))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<FluidShellBlock> FLUID_SHELL = REGISTRATE
		.block("fluid_shell", FluidShellBlock::new)
		.transform(shell(MapColor.COLOR_ORANGE))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/fluid_shell"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot(f -> f.copy("FluidContent", "BlockEntityTag.FluidContent")))
		.item(FluidShellBlockItem::new)
		.transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/fluid_shell"))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<SmokeShellBlock> SMOKE_SHELL = REGISTRATE
		.block("smoke_shell", SmokeShellBlock::new)
		.transform(shell(MapColor.COLOR_LIGHT_BLUE))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.projectile("projectile/smoke_shell"))
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot())
		.item(FuzedProjectileBlockItem::new)
		.transform(CBCBuilderTransformers.fuzedProjectileItem("projectile/smoke_shell"))
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<GrapeshotBlock> BAG_OF_GRAPESHOT = REGISTRATE
		.block("bag_of_grapeshot", GrapeshotBlock::new)
		.properties(p -> p.mapColor(MapColor.WOOL))
		.properties(p -> p.strength(0.8f))
		.properties(p -> p.sound(SoundType.WOOL))
		.transform(CBCBuilderTransformers.projectileLegacy("projectile/grapeshot"))
		.lang("Bag of Grapeshot")
		.item()
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<MortarStoneBlock> MORTAR_STONE = REGISTRATE
		.block("mortar_stone", MortarStoneBlock::new)
		.initialProperties(SharedProperties::stone)
		.transform(CBCBuilderTransformers.projectileLegacy("projectile/mortar_stone"))
		.item(MortarStoneItem::new)
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<Block> MORTAR_STONE_PROJECTILE = REGISTRATE
		.block("mortar_stone_projectile", Block::new)
		.initialProperties(SharedProperties::stone)
		.properties(p -> p.noOcclusion())
		.blockstate(CBCBuilderTransformers.simpleBlock("block/mortar_stone_projectile"))
		.simpleItem()
		.register();

	public static final BlockEntry<DropMortarShellBlock> DROP_MORTAR_SHELL = REGISTRATE
		.block("drop_mortar_shell", DropMortarShellBlock::new)
		.transform(shell(MapColor.COLOR_RED))
		.transform(axeOrPickaxe())
		.transform(CBCBuilderTransformers.dropMortarShell())
		.transform(CBCBuilderTransformers.safeNbt())
		.loot(CBCBuilderTransformers.shellLoot())
		.item(FuzedProjectileBlockItem::new)
		.transform(CBCBuilderTransformers.dropMortarShellItem())
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
		.build()
		.register();

	public static final BlockEntry<PowderChargeBlock> POWDER_CHARGE = REGISTRATE
		.block("powder_charge", PowderChargeBlock::new)
		.initialProperties(() -> Blocks.TNT)
		.properties(p -> p.sound(SoundType.WOOL))
		.transform(CBCBuilderTransformers.powderCharge())
		.item(PowderChargeItem::new)
		.tag(CBCTags.CBCItemTags.BIG_CANNON_PROPELLANT_BAGS)
		.build()
		.onRegister(block -> MunitionPropertiesHandler.registerBlockPropellantHandler(block, CBCMunitionPropertiesHandlers.POWDER_CHARGE))
		.register();

	public static final BlockEntry<BigCartridgeBlock> BIG_CARTRIDGE = REGISTRATE
		.block("big_cartridge", BigCartridgeBlock::new)
		.initialProperties(() -> Blocks.TNT)
		.properties(p -> p.sound(SoundType.METAL))
		.transform(CBCBuilderTransformers.bigCartridge())
		.onRegister(block -> MunitionPropertiesHandler.registerBlockPropellantHandler(block, CBCMunitionPropertiesHandlers.BIG_CARTRIDGE))
		.register();

	//////// Autocannon Ammo Container Blocks ////////

	public static final BlockEntry<AutocannonAmmoContainerBlock> AUTOCANNON_AMMO_CONTAINER = REGISTRATE
		.block("autocannon_ammo_container", AutocannonAmmoContainerBlock::new)
		.properties(p -> p.mapColor(MapColor.COLOR_GREEN))
		.properties(p -> p.strength(0.0f, 2.5f))
		.properties(p -> p.sound(SoundType.CHAIN))
		.properties(p -> p.noOcclusion())
		.transform(CBCBuilderTransformers.autocannonAmmoContainer(false))
		.register();

	public static final BlockEntry<AutocannonAmmoContainerBlock> CREATIVE_AUTOCANNON_AMMO_CONTAINER = REGISTRATE
		.block("creative_autocannon_ammo_container", AutocannonAmmoContainerBlock::new)
		.properties(p -> p.mapColor(MapColor.COLOR_MAGENTA))
		.properties(p -> p.strength(0.0f, 2.5f))
		.properties(p -> p.sound(SoundType.CHAIN))
		.properties(p -> p.noOcclusion())
		.transform(CBCBuilderTransformers.autocannonAmmoContainer(true))
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
		.properties(p -> p.pushReaction(PushReaction.BLOCK))
		.properties(p -> p.noOcclusion())
		.transform(CBCBuilderTransformers.invisibleWithParticle("block/casting_sand"))
		.register();

	public static final BlockEntry<FinishedCannonCastBlock> FINISHED_CANNON_CAST = REGISTRATE
		.block("finished_cannon_cast", FinishedCannonCastBlock::new)
		.transform(castingSand())
		.properties(p -> p.pushReaction(PushReaction.BLOCK))
		.properties(p -> p.noOcclusion())
		.transform(CBCBuilderTransformers.invisibleWithParticle("block/casting_sand"))
		.register();

	public static final BlockEntry<CannonCastMouldBlock> VERY_SMALL_CAST_MOULD =
		castMould("very_small", Block.box(2, 0, 2, 14, 17, 14), () -> CannonCastShape.VERY_SMALL);

	public static final BlockEntry<CannonCastMouldBlock> SMALL_CAST_MOULD =
		castMould("small", Block.box(1, 0, 1, 15, 17, 15), () -> CannonCastShape.SMALL);

	public static final BlockEntry<CannonCastMouldBlock> MEDIUM_CAST_MOULD =
		castMould("medium", Block.box(0, 0, 0, 16, 17, 16), () -> CannonCastShape.MEDIUM);

	public static final BlockEntry<CannonCastMouldBlock> LARGE_CAST_MOULD =
		castMould("large", Block.box(-1, 0, -1, 17, 17, 17), () -> CannonCastShape.LARGE);

	public static final BlockEntry<CannonCastMouldBlock> VERY_LARGE_CAST_MOULD =
		castMould("very_large", Block.box(-2, 0, -2, 18, 17, 18), () -> CannonCastShape.VERY_LARGE);

	public static final BlockEntry<CannonCastMouldBlock> CANNON_END_CAST_MOULD =
		castMould("cannon_end", Shapes.or(Block.box(5, 0, 5, 11, 6, 11), Block.box(6, 6, 6, 10, 8, 10), Block.box(0, 8, 0, 16, 16, 16)), () -> CannonCastShape.CANNON_END);

	public static final BlockEntry<CannonCastMouldBlock> SLIDING_BREECH_CAST_MOULD =
		castMould("sliding_breech", Block.box(0, 0, 0, 16, 17, 16), () -> CannonCastShape.SLIDING_BREECH);

	public static final BlockEntry<CannonCastMouldBlock> SCREW_BREECH_CAST_MOULD =
		castMould("screw_breech", Block.box(0, 8, 0, 16, 17, 16), () -> CannonCastShape.SCREW_BREECH);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_BREECH_CAST_MOULD =
		castMould("autocannon_breech", Block.box(4, 0, 4, 12, 17, 12), () -> CannonCastShape.AUTOCANNON_BREECH);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_RECOIL_SPRING_CAST_MOULD =
		castMould("autocannon_recoil_spring", Block.box(5, 0, 5, 11, 17, 11), () -> CannonCastShape.AUTOCANNON_RECOIL_SPRING);

	public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_BARREL_CAST_MOULD =
		castMould("autocannon_barrel", Block.box(6, 0, 6, 10, 17, 10), () -> CannonCastShape.AUTOCANNON_BARREL);

	public static final BlockEntry<BasinFoundryLidBlock> BASIN_FOUNDRY_LID = REGISTRATE
		.block("basin_foundry_lid", BasinFoundryLidBlock::new)
		.initialProperties(SharedProperties::stone)
		.properties(p -> p.mapColor(MapColor.COLOR_GRAY))
		.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
		.properties(p -> p.noOcclusion())
		.tag(BlockTags.MINEABLE_WITH_PICKAXE)
		.blockstate(CBCBuilderTransformers.simpleBlock("block/basin_foundry_lid"))
		.simpleItem()
		.register();

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBlock() {
		return cannonBlock(true);
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBlock(boolean canPassThrough) {
		NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.strength(5.0f, 6.0f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.properties(p -> p.requiresCorrectToolForDrops())
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.tag(BlockTags.NEEDS_IRON_TOOL);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> logCannonBlock() {
		return logCannonBlock(true);
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> logCannonBlock(boolean canPassThrough) {
		NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.mapColor(MapColor.CRIMSON_STEM))
			.properties(p -> p.strength(2.0f))
			.properties(p -> p.sound(SoundType.WOOD))
			.tag(BlockTags.MINEABLE_WITH_AXE)
			.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> strongCannonBlock() {
		return strongCannonBlock(true);
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> strongCannonBlock(boolean canPassThrough) {
		NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.strength(50.0f, 1200.0f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.properties(p -> p.requiresCorrectToolForDrops())
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.tag(BlockTags.NEEDS_DIAMOND_TOOL);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cbcMachine() {
		return b -> b.initialProperties(() -> Blocks.GOLD_BLOCK)
			.properties(p -> p.mapColor(MapColor.PODZOL))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK));
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> shell(MapColor color) {
		return b -> b.addLayer(() -> RenderType::solid)
			.properties(p -> p.mapColor(color))
			.properties(p -> p.strength(2.0f, 3.0f))
			.properties(p -> p.sound(SoundType.STONE));
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> castingSand() {
		return b -> b.initialProperties(() -> Blocks.SAND)
			.tag(BlockTags.MINEABLE_WITH_SHOVEL);
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> axeOrPickaxe() {
		return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
			.tag(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> axeOnly() {
		return b -> b.tag(BlockTags.MINEABLE_WITH_AXE);
	}

	private static BlockEntry<CannonCastMouldBlock> castMould(String name, VoxelShape blockShape, Supplier<CannonCastShape> castShape) {
		return REGISTRATE.block(name + "_cast_mould", p -> new CannonCastMouldBlock(p, blockShape, castShape))
			.transform(CBCBuilderTransformers.castMould(name))
			.register();
	}

	private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return false;
	}

	private static BlockState castIronAutocannonBarrel(Direction facing) {
		return CAST_IRON_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, facing);
	}

	private static BlockState bronzeAutocannonBarrel(Direction facing) {
		return BRONZE_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, facing);
	}

	private static BlockState steelAutocannonBarrel(Direction facing) {
		return STEEL_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, facing);
	}

	public static void register() {
	}

}
