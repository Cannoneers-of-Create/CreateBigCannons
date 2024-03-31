package rbasamoyai.createbigcannons.compat.copycats;

import static rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties.createMimicrySerializer;

import com.copycatsplus.copycats.CCBlocks;

import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.CopycatBlockArmorProperties;

public class CopycatsCompat {

	public static void init() {
		registerCopycatSerializer(CCBlocks.COPYCAT_BEAM.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_BLOCK.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_BOARD.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_BYTE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_FENCE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_FENCE_GATE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_HALF_LAYER.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_HALF_PANEL.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_LAYER.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_SLAB.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_SLICE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_STAIRS.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_STONE_BUTTON.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_STONE_PRESSURE_PLATE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_TRAPDOOR.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_VERTICAL_SLICE.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_VERTICAL_STEP.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_WALL.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_WOODEN_BUTTON.get());
		registerCopycatSerializer(CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE.get());

	}

	private static void registerCopycatSerializer(Block block) {
		BlockArmorPropertiesHandler.registerCustomSerializer(block, createMimicrySerializer(CopycatBlockArmorProperties::new));
	}

}
