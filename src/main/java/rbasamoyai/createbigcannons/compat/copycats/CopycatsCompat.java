package rbasamoyai.createbigcannons.compat.copycats;

import static rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties.createMimicrySerializer;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.byte_panel.CopycatBytePanelBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesSerializer;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.CopycatBlockArmorProperties;

public class CopycatsCompat {

	public static void init(Function<String, Block> blockGetter) {
		registerCopycatSerializer(blockGetter.apply("copycat_block"));
		registerCopycatSerializer(blockGetter.apply("copycat_beam"));
        registerCopycatSerializer(blockGetter.apply("copycat_corner_slice"));
        registerCopycatSerializer(blockGetter.apply("copycat_door"));
        registerCopycatSerializer(blockGetter.apply("copycat_fence"));
        registerCopycatSerializer(blockGetter.apply("copycat_fence_gate"));
        registerCopycatSerializer(blockGetter.apply("copycat_flat_pane"));
        registerCopycatSerializer(blockGetter.apply("copycat_fluid_pipe"));
        registerCopycatSerializer(blockGetter.apply("copycat_folding_door"));
        registerCopycatSerializer(blockGetter.apply("copycat_ghost_block"));
        registerCopycatSerializer(blockGetter.apply("copycat_glass_fluid_pipe"));
        registerCopycatSerializer(blockGetter.apply("copycat_half_panel"));
        registerCopycatSerializer(blockGetter.apply("copycat_heavy_weighted_pressure_plate"));
        registerCopycatSerializer(blockGetter.apply("copycat_iron_door"));
        registerCopycatSerializer(blockGetter.apply("copycat_iron_trapdoor"));
        registerCopycatSerializer(blockGetter.apply("copycat_ladder"));
        registerCopycatSerializer(blockGetter.apply("copycat_layer"));
        registerCopycatSerializer(blockGetter.apply("copycat_light_weighted_pressure_plate"));
        registerCopycatSerializer(blockGetter.apply("copycat_pane"));
        registerCopycatSerializer(blockGetter.apply("copycat_shaft"));
        registerCopycatSerializer(blockGetter.apply("copycat_slice"));
        registerCopycatSerializer(blockGetter.apply("copycat_sliding_door"));
        registerCopycatSerializer(blockGetter.apply("copycat_slope"));
        registerCopycatSerializer(blockGetter.apply("copycat_slope_layer"));
        registerCopycatSerializer(blockGetter.apply("copycat_stairs"));
        registerCopycatSerializer(blockGetter.apply("copycat_stone_button"));
        registerCopycatSerializer(blockGetter.apply("copycat_stone_pressure_plate"));
        registerCopycatSerializer(blockGetter.apply("copycat_trapdoor"));
        registerCopycatSerializer(blockGetter.apply("copycat_vertical_slice"));
        registerCopycatSerializer(blockGetter.apply("copycat_vertical_slope"));
        registerCopycatSerializer(blockGetter.apply("copycat_vertical_stairs"));
        registerCopycatSerializer(blockGetter.apply("copycat_vertical_step"));
        registerCopycatSerializer(blockGetter.apply("copycat_wall"));
		registerCopycatSerializer(blockGetter.apply("copycat_wooden_button"));
		registerCopycatSerializer(blockGetter.apply("copycat_wooden_pressure_plate"));

        registerMultistateCopycatSerializer(blockGetter.apply("copycat_board"), BinaryCopycatArmorProperties.of(CopycatBoardBlock.UP, CopycatBoardBlock.DOWN, CopycatBoardBlock.NORTH, CopycatBoardBlock.EAST, CopycatBoardBlock.SOUTH, CopycatBoardBlock.WEST));
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_byte"), BinaryCopycatArmorProperties.of(CopycatByteBlock.TOP_NE, CopycatByteBlock.TOP_SE, CopycatByteBlock.TOP_SW, CopycatByteBlock.TOP_NW, CopycatByteBlock.BOTTOM_NE, CopycatByteBlock.BOTTOM_SE, CopycatByteBlock.BOTTOM_SW, CopycatByteBlock.BOTTOM_NW));
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_byte_panel"), BinaryCopycatArmorProperties.of(CopycatBytePanelBlock.TOP_LEFT, CopycatBytePanelBlock.TOP_RIGHT, CopycatBytePanelBlock.BOTTOM_RIGHT, CopycatBytePanelBlock.BOTTOM_LEFT));
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_cogwheel"), CopycatCogwheelArmorProperties::new);
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_half_layer"), CopycatHalfLayerArmorProperties::new);
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_large_cogwheel"), CopycatCogwheelArmorProperties::new);
        registerMultistateCopycatSerializer(blockGetter.apply("copycat_slab"), CopycatSlabArmorProperties::new);
	}

	private static void registerCopycatSerializer(@Nullable Block block) {
        registerSpecialCopycatSerializer(block, createMimicrySerializer(CopycatBlockArmorProperties::new));
	}

    private static void registerMultistateCopycatSerializer(@Nullable Block block, MultiStateCopycatArmorProperties.Factory<?> fac) {
        registerSpecialCopycatSerializer(block, MultiStateCopycatArmorProperties.createMultistateSerializer(fac));
    }

    private static void registerSpecialCopycatSerializer(@Nullable Block block, BlockArmorPropertiesSerializer<?> ser) {
        if (block != null & block != Blocks.AIR)
            BlockArmorPropertiesHandler.registerCustomSerializer(block, ser);
    }

}
