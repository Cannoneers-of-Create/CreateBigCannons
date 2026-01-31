package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesSerializer;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class FramedAdjustibleDoubleBlockArmorPropertiesSerializer implements BlockArmorPropertiesSerializer<FramedAdjustableDoubleBlockArmorProperties> {

	private static final MimickingBlockArmorUnit FALLBACK = new MimickingBlockArmorUnit(0.5d, 0.5d, 0.5d, 0.5d);

	@Override
	public FramedAdjustableDoubleBlockArmorProperties loadBlockArmorPropertiesFromJson(Block block, JsonObject obj) {
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
		return new FramedAdjustableDoubleBlockArmorProperties(primaryUnit, secondaryUnit, primaryPropertiesByState, secondaryPropertiesByState);
	}

	@Override
	public void toNetwork(FramedAdjustableDoubleBlockArmorProperties properties, FriendlyByteBuf buf) {
		properties.getPrimaryDefaultProperties().toNetwork(buf);
		properties.getSecondaryDefaultProperties().toNetwork(buf);
		MimickingBlockArmorUnit.writePropertiesToBuf(properties.getPrimaryPropertiesByState(), buf);
		MimickingBlockArmorUnit.writePropertiesToBuf(properties.getSecondaryPropertiesByState(), buf);
	}

	@Override
	public FramedAdjustableDoubleBlockArmorProperties fromNetwork(FriendlyByteBuf buf) {
		MimickingBlockArmorUnit defaultPrimaryProperties = MimickingBlockArmorUnit.fromNetwork(buf);
		MimickingBlockArmorUnit defaultSecondaryProperties = MimickingBlockArmorUnit.fromNetwork(buf);
		Map<BlockState, MimickingBlockArmorUnit> primaryPropertiesByState = MimickingBlockArmorUnit.readPropertiesFromBuf(buf);
		Map<BlockState, MimickingBlockArmorUnit> secondaryPropertiesByState = MimickingBlockArmorUnit.readPropertiesFromBuf(buf);
		return new FramedAdjustableDoubleBlockArmorProperties(defaultPrimaryProperties, defaultSecondaryProperties, primaryPropertiesByState, secondaryPropertiesByState);
	}

}
