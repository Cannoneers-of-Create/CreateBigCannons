package rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import rbasamoyai.createbigcannons.base.BlockStatePredicateHelper;

public record MimickingBlockArmorUnit(double emptyHardness, double materialHardnessMultiplier) {

	public static MimickingBlockArmorUnit fromJson(JsonObject obj) {
		double emptyHardness = Math.max(GsonHelper.getAsDouble(obj, "default_block_hardness", 1d), 0d);
		double materialHardnessMultiplier = Math.max(GsonHelper.getAsDouble(obj, "material_hardness_multiplier", 1d), 0d);
		return new MimickingBlockArmorUnit(emptyHardness, materialHardnessMultiplier);
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.emptyHardness)
			.writeDouble(this.materialHardnessMultiplier);
	}

	public static MimickingBlockArmorUnit fromNetwork(FriendlyByteBuf buf) {
		double emptyHardness = buf.readDouble();
		double materialHardnessMultiplier = buf.readDouble();
		return new MimickingBlockArmorUnit(emptyHardness, materialHardnessMultiplier);
	}

	public static Map<BlockState, MimickingBlockArmorUnit> readAllProperties(Block block, JsonObject obj) {
		StateDefinition<Block, BlockState> definition = block.getStateDefinition();
		Set<BlockState> states = new HashSet<>(definition.getPossibleStates());
		Map<BlockState, MimickingBlockArmorUnit> propertiesByState = new Reference2ObjectOpenHashMap<>();
		for (String key : obj.keySet()) {
			Predicate<BlockState> pred = BlockStatePredicateHelper.variantPredicate(definition, key);
			JsonElement el = obj.get(key);
			if (!el.isJsonObject()) {
				throw new JsonSyntaxException("Invalid info for variant '" + key + "''");
			}
			JsonObject variantInfo = el.getAsJsonObject();
			MimickingBlockArmorUnit properties = MimickingBlockArmorUnit.fromJson(variantInfo);
			for (Iterator<BlockState> stateIter = states.iterator(); stateIter.hasNext(); ) {
				BlockState state1 = stateIter.next();
				if (pred.test(state1)) {
					propertiesByState.put(state1, properties);
					stateIter.remove();
				}
			}
		}
		return propertiesByState;
	}

	public static void writePropertiesToBuf(Map<BlockState, MimickingBlockArmorUnit> map, FriendlyByteBuf buf) {
		buf.writeVarInt(map.size());
		for (Map.Entry<BlockState, MimickingBlockArmorUnit> entry : map.entrySet()) {
			buf.writeVarInt(Block.getId(entry.getKey()));
			entry.getValue().toNetwork(buf);
		}
	}

	public static Map<BlockState, MimickingBlockArmorUnit> readPropertiesFromBuf(FriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		Map<BlockState, MimickingBlockArmorUnit> map = new Reference2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i) {
			BlockState state = Block.stateById(buf.readVarInt());
			MimickingBlockArmorUnit properties = MimickingBlockArmorUnit.fromNetwork(buf);
			map.put(state, properties);
		}
		return map;
	}

}
