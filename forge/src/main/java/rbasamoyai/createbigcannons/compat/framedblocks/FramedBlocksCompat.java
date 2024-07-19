package rbasamoyai.createbigcannons.compat.framedblocks;

import static rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties.createMimicrySerializer;

import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;

public class FramedBlocksCompat {

	public static void init() {
		for (BlockType type : BlockType.values()) {
			if (type == BlockType.FRAMED_COLLAPSIBLE_BLOCK) {
				BlockArmorPropertiesHandler.registerCustomSerializer(FBContent.byType(type), createMimicrySerializer(FramedCollapsibleBlockArmorProperties::new));
			} else if (type == BlockType.FRAMED_COLLAPSIBLE_COPYCAT_BLOCK) {
				BlockArmorPropertiesHandler.registerCustomSerializer(FBContent.byType(type), createMimicrySerializer(FramedCollapsibleCopycatBlockArmorProperties::new));
			} else if (type == BlockType.FRAMED_ADJ_DOUBLE_COPYCAT_PANEL || type == BlockType.FRAMED_ADJ_DOUBLE_COPYCAT_SLAB) {
				BlockArmorPropertiesHandler.registerCustomSerializer(FBContent.byType(type), new FramedAdjustibleDoubleBlockArmorPropertiesSerializer());
			} else if (type.isDoubleBlock()) {
				registerDoubleFramedBlockSerializer(type);
			} else {
				registerSingleFramedBlockSerializer(type);
			}
		}
	}

	private static void registerSingleFramedBlockSerializer(BlockType block) {
		BlockArmorPropertiesHandler.registerCustomSerializer(FBContent.byType(block), createMimicrySerializer(SingleFramedBlockArmorProperties::new));
	}

	private static void registerDoubleFramedBlockSerializer(BlockType block) {
		BlockArmorPropertiesHandler.registerCustomSerializer(FBContent.byType(block), new FramedDoubleBlockArmorPropertiesSerializer());
	}

}
