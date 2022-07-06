package rbasamoyai.createbigcannons;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import rbasamoyai.createbigcannons.cannons.CannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.CannonChamberBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterials;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;

public class CBCBlocks {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate()
			.creativeModeTab(() -> ModGroups.MOD_GROUP);
	
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
	
	private static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> castIron() {
		return b -> b.initialProperties(Material.METAL)
				.properties(p -> p.strength(5.0f, 6.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_IRON_TOOL);
	}
	
	public static void register() {}
	
}
