package rbasamoyai.createbigcannons.compat.create;

import static rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties.createMimicrySerializer;

import com.simibubi.create.AllBlocks;

import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.CopycatBlockArmorProperties;

public class DefaultCreateCompat {

	public static void init() {
		registerCopycatSerializer(AllBlocks.COPYCAT_PANEL.get());
		registerCopycatSerializer(AllBlocks.COPYCAT_STEP.get());
	}

	private static void registerCopycatSerializer(Block block) {
		BlockArmorPropertiesHandler.registerCustomSerializer(block, createMimicrySerializer(CopycatBlockArmorProperties::new));
	}

}
