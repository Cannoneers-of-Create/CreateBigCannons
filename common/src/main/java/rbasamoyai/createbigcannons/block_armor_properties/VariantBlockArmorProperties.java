package rbasamoyai.createbigcannons.block_armor_properties;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import rbasamoyai.createbigcannons.base.BlockStatePredicateHelper;

public record VariantBlockArmorProperties(SimpleBlockArmorProperties defaultProperties, Map<BlockState, SimpleBlockArmorProperties> propertiesByState)
	implements BlockArmorPropertiesProvider {

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return this.propertiesByState.getOrDefault(state, this.defaultProperties).hardness(level, state, pos, recurse);
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return this.propertiesByState.getOrDefault(state, this.defaultProperties).toughness(level, state, pos, recurse);
	}

	public static VariantBlockArmorProperties fromJson(Block block, JsonObject obj) {
		StateDefinition<Block, BlockState> definition = block.getStateDefinition();
		Set<BlockState> states = new HashSet<>(definition.getPossibleStates());
		Map<BlockState, SimpleBlockArmorProperties> propertiesByState = new Reference2ObjectOpenHashMap<>();

		if (obj.has("variants") && obj.get("variants").isJsonObject()) {
			JsonObject variants = obj.getAsJsonObject("variants");
			for (String key : variants.keySet()) {
				Predicate<BlockState> pred = BlockStatePredicateHelper.variantPredicate(definition, key);
				JsonElement el = variants.get(key);
				if (!el.isJsonObject()) {
					throw new JsonSyntaxException("Invalid info for variant '" + key + "''");
				}
				JsonObject variantInfo = el.getAsJsonObject();
				SimpleBlockArmorProperties properties = SimpleBlockArmorProperties.fromJson(variantInfo);
				for (Iterator<BlockState> stateIter = states.iterator(); stateIter.hasNext(); ) {
					BlockState state = stateIter.next();
					if (pred.test(state)) {
						propertiesByState.put(state, properties);
						stateIter.remove();
					}
				}
			}
		}
		SimpleBlockArmorProperties defaultProperties = SimpleBlockArmorProperties.fromJson(obj);
		return new VariantBlockArmorProperties(defaultProperties, propertiesByState);
	}

	public void toNetwork(FriendlyByteBuf buf) {
		this.defaultProperties.toNetwork(buf);
		buf.writeVarInt(this.propertiesByState.size());
		for (Map.Entry<BlockState, SimpleBlockArmorProperties> entry : this.propertiesByState.entrySet()) {
			buf.writeVarInt(Block.getId(entry.getKey()));
			entry.getValue().toNetwork(buf);
		}
	}

	public static VariantBlockArmorProperties fromNetwork(FriendlyByteBuf buf) {
		SimpleBlockArmorProperties defaultProperties = SimpleBlockArmorProperties.fromNetwork(buf);
		int sz = buf.readVarInt();
		Map<BlockState, SimpleBlockArmorProperties> propertiesByState = new Reference2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i) {
			BlockState state = Block.stateById(buf.readVarInt());
			SimpleBlockArmorProperties properties = SimpleBlockArmorProperties.fromNetwork(buf);
			propertiesByState.put(state, properties);
		}
		return new VariantBlockArmorProperties(defaultProperties, propertiesByState);
	}

}
