package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesSerializer;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class FramedDoubleBlockArmorPropertiesSerializer implements BlockArmorPropertiesSerializer {

	private static final MimickingBlockArmorUnit FALLBACK = new MimickingBlockArmorUnit(0.5d, 0.5d);

	@Override
	public BlockArmorPropertiesProvider loadBlockArmorPropertiesFromJson(Block block, String id, JsonObject obj) {
		MimickingBlockArmorUnit primaryUnit = FALLBACK;
		Map<BlockState, MimickingBlockArmorUnit> primaryPropertiesByState = new Reference2ObjectOpenHashMap<>();
		if (obj.has("primary") && obj.get("primary").isJsonObject()) {
			JsonObject primary = obj.getAsJsonObject("primary");
			if (primary.has("variants") && primary.get("variants").isJsonObject()) {
				primaryPropertiesByState.putAll(MimickingBlockArmorUnit.readAllProperties(block, primary.getAsJsonObject("variants")));
			}
			primaryUnit = MimickingBlockArmorUnit.fromJson(primary);
		}
		MimickingBlockArmorUnit secondaryUnit = FALLBACK;
		Map<BlockState, MimickingBlockArmorUnit> secondaryPropertiesByState = new Reference2ObjectOpenHashMap<>();
		if (obj.has("secondary") && obj.get("secondary").isJsonObject()) {
			JsonObject secondary = obj.getAsJsonObject("secondary");
			if (secondary.has("variants") && secondary.get("variants").isJsonObject()) {
				secondaryPropertiesByState.putAll(MimickingBlockArmorUnit.readAllProperties(block, secondary.getAsJsonObject("variants")));
			}
			secondaryUnit = MimickingBlockArmorUnit.fromJson(secondary);
		}
		return new FramedDoubleBlockArmorProperties(primaryUnit, secondaryUnit, primaryPropertiesByState, secondaryPropertiesByState);
	}

}
