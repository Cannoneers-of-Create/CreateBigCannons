package rbasamoyai.createbigcannons;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannonloading.RamHeadBlock;
import rbasamoyai.createbigcannons.cannons.CannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.CannonChamberBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterials;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.munitions.PowderChargeBlock;

public class CBCBlocks {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate()
			.creativeModeTab(() -> ModGroup.GROUP);
	
	public static final BlockEntry<CannonBarrelBlock> CAST_IRON_CANNON_BARREL = REGISTRATE
			.block("cast_iron_cannon_barrel", p -> new CannonBarrelBlock(p, CannonMaterials.CAST_IRON))
			.transform(castIron())
			.transform(CBCBuilderTransformers.cannonBarrel("cast_iron"))
			.simpleItem()
			.register();
	
	public static final BlockEntry<CannonChamberBlock> CAST_IRON_CANNON_CHAMBER = REGISTRATE
			.block("cast_iron_cannon_chamber", p -> new CannonChamberBlock(p, CannonMaterials.CAST_IRON))
			.transform(castIron())
			.transform(CBCBuilderTransformers.cannonChamber("cast_iron"))
			.simpleItem()
			.register();
	
	public static final BlockEntry<CannonLoaderBlock> CANNON_LOADER = REGISTRATE
			.block("cannon_loader", CannonLoaderBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.cannonLoader())
			.item()
			.model(CBCBuilderTransformers.cannonLoaderItemModel())
			.build()
			.register();
	
	public static final BlockEntry<RamHeadBlock> RAM_HEAD = REGISTRATE
			.block("ram_head", RamHeadBlock::new)
			.initialProperties(() -> Blocks.PISTON_HEAD)
			.properties(p -> p.sound(SoundType.WOOD))
			.transform(CBCBuilderTransformers.ramHead())
			.transform(axeOrPickaxe())
			.simpleItem()
			.register();
	
	public static final BlockEntry<PowderChargeBlock> POWDER_CHARGE = REGISTRATE
			.block("powder_charge", PowderChargeBlock::new)
			.initialProperties(() -> Blocks.TNT)
			.properties(p -> p.sound(SoundType.WOOL))
			.transform(CBCBuilderTransformers.powderCharge())
			.simpleItem()
			.register();
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> castIron() {
		return b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(5.0f, 6.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_IRON_TOOL);
	}
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
		return b -> b.tag(BlockTags.MINEABLE_WITH_AXE)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE);
	}
	
	public static void register() {}
	
}
