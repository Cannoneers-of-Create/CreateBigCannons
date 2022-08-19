package rbasamoyai.createbigcannons;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
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
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlock;
import rbasamoyai.createbigcannons.cannonmount.YawControllerBlock;
import rbasamoyai.createbigcannons.cannons.CannonBlockItem;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.CannonTubeBlock;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlock;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlock;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.DrillBitBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredCannonBlock;
import rbasamoyai.createbigcannons.crafting.boring.UnboredSlidingBreechBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteSlidingBreechBlock;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.munitions.PowderChargeBlock;
import rbasamoyai.createbigcannons.munitions.grapeshot.GrapeshotBlock;
import rbasamoyai.createbigcannons.munitions.heshell.HEShellBlock;
import rbasamoyai.createbigcannons.munitions.shot.SolidShotBlock;
import rbasamoyai.createbigcannons.munitions.shrapnel.ShrapnelShellBlock;

public class CBCBlocks {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate()
			.creativeModeTab(() -> ModGroup.GROUP);
	
	static {
		REGISTRATE.startSection(AllSections.KINETICS);
	}
	
	//////// Log cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> LOG_CANNON_CHAMBER = REGISTRATE
			.block("log_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.LOG))
			.transform(logCannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/log"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonEndBlock> LOG_CANNON_END = REGISTRATE
			.block("log_cannon_end", p -> new CannonEndBlock(p, CannonMaterial.LOG))
			.transform(logCannonBlock())
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/log"))
			.item(CannonBlockItem::new).build()
			.register();
	
	//////// Wrought Iron cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> WROUGHT_IRON_CANNON_CHAMBER = REGISTRATE
			.block("wrought_iron_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.WROUGHT_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/wrought_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonEndBlock> WROUGHT_IRON_CANNON_END = REGISTRATE
			.block("wrought_iron_cannon_end", p -> new CannonEndBlock(p, CannonMaterial.WROUGHT_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/wrought_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	//////// Cast Iron cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> CAST_IRON_CANNON_BARREL = REGISTRATE
			.block("cast_iron_cannon_barrel", p -> CannonTubeBlock.verySmall(p, CannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonTubeBlock> CAST_IRON_CANNON_CHAMBER = REGISTRATE
			.block("cast_iron_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonEndBlock> CAST_IRON_CANNON_END = REGISTRATE
			.block("cast_iron_cannon_end", p -> new CannonEndBlock(p, CannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("cast_iron_sliding_breech", p -> new SlidingBreechBlock(p, CannonMaterial.CAST_IRON))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/cast_iron"))
			.transform(BlockStressDefaults.setImpact(16.0d))
			.register();
	
	public static final BlockEntry<UnboredCannonBlock> UNBORED_CAST_IRON_CANNON_BARREL = REGISTRATE
			.block("unbored_cast_iron_cannon_barrel", p -> UnboredCannonBlock.verySmall(p, CannonMaterial.CAST_IRON, CAST_IRON_CANNON_BARREL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/cast_iron", "cannon_barrel/unbored_cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredCannonBlock> UNBORED_CAST_IRON_CANNON_CHAMBER = REGISTRATE
			.block("unbored_cast_iron_cannon_chamber", p -> UnboredCannonBlock.medium(p, CannonMaterial.CAST_IRON, CAST_IRON_CANNON_CHAMBER))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/cast_iron", "cannon_chamber/unbored_cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<IncompleteSlidingBreechBlock> INCOMPLETE_CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("incomplete_cast_iron_sliding_breech", p -> new IncompleteSlidingBreechBlock(p, CannonMaterial.CAST_IRON, CBCItems.CAST_IRON_SLIDING_BREECHBLOCK, CAST_IRON_SLIDING_BREECH))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreechIncomplete("sliding_breech/cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<UnboredSlidingBreechBlock> UNBORED_CAST_IRON_SLIDING_BREECH = REGISTRATE
			.block("unbored_cast_iron_sliding_breech", p -> new UnboredSlidingBreechBlock(p, CannonMaterial.CAST_IRON, INCOMPLETE_CAST_IRON_SLIDING_BREECH, Shapes.block()))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreechUnbored("sliding_breech/unbored_cast_iron"))
			.item(CannonBlockItem::new).build()
			.register();
	
	//////// Bronze cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> BRONZE_CANNON_BARREL = REGISTRATE
			.block("bronze_cannon_barrel", p -> CannonTubeBlock.verySmall(p, CannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/bronze"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonTubeBlock> BRONZE_CANNON_CHAMBER = REGISTRATE
			.block("bronze_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/bronze"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonEndBlock> BRONZE_CANNON_END = REGISTRATE
			.block("bronze_cannon_end", p -> new CannonEndBlock(p, CannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonEnd("cannon_end/bronze"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> BRONZE_SLIDING_BREECH = REGISTRATE
			.block("bronze_sliding_breech", p -> new SlidingBreechBlock(p, CannonMaterial.BRONZE))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/bronze"))
			.transform(BlockStressDefaults.setImpact(12.0d))
			.register();
	
	//////// Steel cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> STEEL_CANNON_BARREL = REGISTRATE
			.block("steel_cannon_barrel", p -> CannonTubeBlock.verySmall(p, CannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/steel"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonTubeBlock> STEEL_CANNON_CHAMBER = REGISTRATE
			.block("steel_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/steel"))
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<SlidingBreechBlock> STEEL_SLIDING_BREECH = REGISTRATE
			.block("steel_sliding_breech", p -> new SlidingBreechBlock(p, CannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/steel"))
			.transform(BlockStressDefaults.setImpact(32.0d))
			.register();
	
	public static final BlockEntry<ScrewBreechBlock> STEEL_SCREW_BREECH = REGISTRATE
			.block("steel_screw_breech", p -> new ScrewBreechBlock(p, CannonMaterial.STEEL))
			.transform(cannonBlock())
			.transform(CBCBuilderTransformers.screwBreech("screw_breech/steel"))
			.transform(BlockStressDefaults.setImpact(16.0d))
			.register();
	
	//////// Nethersteel "Nether Gunmetal" cannon blocks ////////
	
	public static final BlockEntry<CannonTubeBlock> NETHER_GUNMETAL_CANNON_BARREL = REGISTRATE
			.block("nether_gunmetal_cannon_barrel", p -> CannonTubeBlock.verySmall(p, CannonMaterial.NETHER_GUNMETAL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonBarrel("cannon_barrel/nether_gunmetal"))
			.lang("Nethersteel Cannon Barrel")
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<CannonTubeBlock> NETHER_GUNMETAL_CANNON_CHAMBER = REGISTRATE
			.block("nether_gunmetal_cannon_chamber", p -> CannonTubeBlock.medium(p, CannonMaterial.NETHER_GUNMETAL))
			.transform(strongCannonBlock())
			.transform(CBCBuilderTransformers.cannonChamber("cannon_chamber/nether_gunmetal"))
			.lang("Nethersteel Cannon Chamber")
			.item(CannonBlockItem::new).build()
			.register();
	
	public static final BlockEntry<ScrewBreechBlock> NETHER_GUNMETAL_SCREW_BREECH = REGISTRATE
			.block("nether_gunmetal_screw_breech", p -> new ScrewBreechBlock(p, CannonMaterial.NETHER_GUNMETAL))
			.transform(strongCannonBlock())
			.lang("Nethersteel Screw Breech")
			.transform(CBCBuilderTransformers.screwBreech("screw_breech/nether_gunmetal"))
			.transform(BlockStressDefaults.setImpact(40.0d))
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
	
	//////// Cannon boring blocks ////////
	
	public static final BlockEntry<CannonDrillBlock> CANNON_DRILL = REGISTRATE
			.block("cannon_drill", CannonDrillBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonDrill())
			.transform(BlockStressDefaults.setImpact(8.0d))
			.register();
	
	public static final BlockEntry<DrillBitBlock> CANNON_DRILL_BIT = REGISTRATE
			.block("cannon_drill_bit", DrillBitBlock::new)
			.properties(p -> p.color(MaterialColor.STONE))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonDrillBit())
			.register();
	
	static {
		REGISTRATE.startSection(AllSections.LOGISTICS);
	}
	
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
			.lang("High Explosive (HE) Shell")
			.simpleItem()
			.register();
	
	public static final BlockEntry<ShrapnelShellBlock> SHRAPNEL_SHELL = REGISTRATE
			.block("shrapnel_shell", ShrapnelShellBlock::new)
			.transform(shell(MaterialColor.COLOR_GREEN))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shrapnel_shell"))
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
	
	public static final BlockEntry<CannonCastMouldBlock> UNBORED_SLIDING_BREECH_CAST_MOULD =
			castMould("unbored_sliding_breech", Block.box(0, 0, 0, 16, 17, 16), CannonCastShape.UNBORED_SLIDING_BREECH);
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> cannonBlock() {
		return b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(5.0f, 6.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_IRON_TOOL);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> logCannonBlock() {
		return b -> b.initialProperties(Material.WOOD, MaterialColor.CRIMSON_STEM)
				.properties(p -> p.strength(2.0f))
				.properties(p -> p.sound(SoundType.WOOD))
				.tag(BlockTags.MINEABLE_WITH_AXE);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> strongCannonBlock() {
		return b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(50.0f, 1200.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_DIAMOND_TOOL);
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
	
	private static BlockEntry<CannonCastMouldBlock> castMould(String name, VoxelShape blockShape, CannonCastShape castShape) {
		return REGISTRATE.block(name + "_cast_mould", p -> new CannonCastMouldBlock(p, blockShape, castShape))
				.transform(CBCBuilderTransformers.castMould(name))
				.register();
	}
	
	private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) { return false; }
	
	public static void register() {}
	
}
