package rbasamoyai.createbigcannons.compat.copycats;

import static rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties.createMimicrySerializer;

import java.util.function.Function;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.CopycatBlockArmorProperties;

public class CopycatsCompat {

	public static void init(Function<String, Block> blockGetter) {
		registerCopycatSerializer(blockGetter.apply("copycat_block"));
		registerCopycatSerializer(blockGetter.apply("copycat_beam"));
		registerCopycatSerializer(blockGetter.apply("copycat_board"));
		registerCopycatSerializer(blockGetter.apply("copycat_wooden_button"));
		registerCopycatSerializer(blockGetter.apply("copycat_stone_button"));
		registerCopycatSerializer(blockGetter.apply("copycat_byte"));
		registerCopycatSerializer(blockGetter.apply("copycat_fence"));
		registerCopycatSerializer(blockGetter.apply("copycat_fence_gate"));
		registerCopycatSerializer(blockGetter.apply("copycat_half_layer"));
		registerCopycatSerializer(blockGetter.apply("copycat_half_panel"));
		registerCopycatSerializer(blockGetter.apply("copycat_ladder"));
		registerCopycatSerializer(blockGetter.apply("copycat_layer"));
		registerCopycatSerializer(blockGetter.apply("copycat_wooden_pressure_plate"));
		registerCopycatSerializer(blockGetter.apply("copycat_stone_pressure_plate"));
		registerCopycatSerializer(blockGetter.apply("copycat_heavy_weighted_pressure_plate"));
		registerCopycatSerializer(blockGetter.apply("copycat_light_weighted_pressure_plate"));
		registerCopycatSerializer(blockGetter.apply("copycat_slab"));
		registerCopycatSerializer(blockGetter.apply("copycat_slice"));
		registerCopycatSerializer(blockGetter.apply("copycat_stairs"));
		registerCopycatSerializer(blockGetter.apply("copycat_vertical_stairs"));
		registerCopycatSerializer(blockGetter.apply("copycat_trapdoor"));
		registerCopycatSerializer(blockGetter.apply("copycat_iron_trapdoor"));
		registerCopycatSerializer(blockGetter.apply("copycat_vertical_slice"));
		registerCopycatSerializer(blockGetter.apply("copycat_vertical_step"));
		registerCopycatSerializer(blockGetter.apply("copycat_wall"));
		registerCopycatSerializer(blockGetter.apply("copycat_slope"));
		registerCopycatSerializer(blockGetter.apply("copycat_vertical_slope"));
		registerCopycatSerializer(blockGetter.apply("copycat_slope_layer"));
		registerCopycatSerializer(blockGetter.apply("copycat_door"));
		registerCopycatSerializer(blockGetter.apply("copycat_iron_door"));
		registerCopycatSerializer(blockGetter.apply("copycat_fluid_pipe"));
		registerCopycatSerializer(blockGetter.apply("copycat_glass_fluid_pipe"));
		registerCopycatSerializer(blockGetter.apply("copycat_shaft"));
		registerCopycatSerializer(blockGetter.apply("copycat_cogwheel"));
		registerCopycatSerializer(blockGetter.apply("copycat_large_cogwheel"));
	}

	private static void registerCopycatSerializer(@Nullable Block block) {
		if (block != null && block != Blocks.AIR)
			BlockArmorPropertiesHandler.registerCustomSerializer(block, createMimicrySerializer(CopycatBlockArmorProperties::new));
	}

}
