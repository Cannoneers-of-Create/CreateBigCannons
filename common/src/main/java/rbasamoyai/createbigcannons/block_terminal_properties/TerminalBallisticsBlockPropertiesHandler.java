package rbasamoyai.createbigcannons.block_terminal_properties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import rbasamoyai.createbigcannons.base.BlockStatePredicateHelper;

public class TerminalBallisticsBlockPropertiesHandler {

	private static final Map<Block, SimpleTerminalBallisticsBlockProperties> TAG_MAP = new HashMap<>();
	private static final Map<BlockState, SimpleTerminalBallisticsBlockProperties> SIMPLE_BLOCK_MAP = new HashMap<>();
	private static final Map<TagKey<Block>, SimpleTerminalBallisticsBlockProperties> TAGS_TO_EVALUATE = new LinkedHashMap<>();

	private static final TerminalBallisticsBlockPropertiesProvider FALLBACK_PROVIDER = new TerminalBallisticsBlockPropertiesProvider() {
		@Override public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) { return state.getBlock().getExplosionResistance(); }
	};

	public static class BlockReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final BlockReloadListener INSTANCE = new BlockReloadListener();

		public BlockReloadListener() { super(GSON, "block_terminal_ballistics"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					if (loc.getPath().startsWith("tags/")) {
						ResourceLocation pruned = new ResourceLocation(loc.getNamespace(), loc.getPath().substring(5));
						TagKey<Block> tag = TagKey.create(Registry.BLOCK_REGISTRY, pruned);
						SimpleTerminalBallisticsBlockProperties properties = SimpleTerminalBallisticsBlockProperties.fromJson("#" + loc, el.getAsJsonObject());
						TAGS_TO_EVALUATE.put(tag, properties);
					} else {
						Block block = Registry.BLOCK.getOptional(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						if (block instanceof HasSpecialTerminalBallisticsBlockProperties special) {
							special.loadTerminalBallisticsBlockPropertiesFromJson(loc.toString(), el.getAsJsonObject());
						} else {
							loadSimpleProperties(block, loc, el.getAsJsonObject());
						}
					}
				} catch (Exception e) {
					//CreateBigCannons.LOGGER.warn("Exception loading terminal ballistics block properties: {}", e.getMessage());
					//Commented out for silent "optional" loading if a bit silly
				}
			}
		}
	}

	private static void loadSimpleProperties(Block block, ResourceLocation loc, JsonObject obj) {
		StateDefinition<Block, BlockState> definition = block.getStateDefinition();
		Set<BlockState> states = new HashSet<>(definition.getPossibleStates());
		if (obj.has("variants") && obj.get("variants").isJsonObject()) {
			JsonObject variants = obj.getAsJsonObject("variants");
			for (String key : variants.keySet()) {
				Predicate<BlockState> pred = BlockStatePredicateHelper.variantPredicate(definition, key);
				JsonElement el = variants.get(key);
				if (!el.isJsonObject()) {
					throw new JsonSyntaxException("Invalid info for variant '" + key + "''");
				}
				JsonObject variantInfo = el.getAsJsonObject();
				SimpleTerminalBallisticsBlockProperties properties = SimpleTerminalBallisticsBlockProperties.fromJson(loc.toString(), variantInfo);
				for (Iterator<BlockState> stateIter = states.iterator(); stateIter.hasNext(); ) {
					BlockState state = stateIter.next();
					if (pred.test(state)) {
						SIMPLE_BLOCK_MAP.put(state, properties);
						stateIter.remove();
					}
				}
			}
		} else {
			SimpleTerminalBallisticsBlockProperties properties = SimpleTerminalBallisticsBlockProperties.fromJson(loc.toString(), obj);
			for (BlockState state : states) {
				SIMPLE_BLOCK_MAP.put(state, properties);
			}
		}
	}

	public static void loadTags() {
		TAG_MAP.clear();
		for (Map.Entry<TagKey<Block>, SimpleTerminalBallisticsBlockProperties> entry : TAGS_TO_EVALUATE.entrySet()) {
			SimpleTerminalBallisticsBlockProperties properties = entry.getValue();
			for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(entry.getKey())) {
				TAG_MAP.put(holder.value(), properties);
			}
		}
		TAGS_TO_EVALUATE.clear();
	}

	public static void cleanUp() {
		TAG_MAP.clear();
		SIMPLE_BLOCK_MAP.clear();
		TAGS_TO_EVALUATE.clear();
	}

	public static TerminalBallisticsBlockPropertiesProvider getProperties(BlockState state) {
		Block block = state.getBlock();
		if (block instanceof HasSpecialTerminalBallisticsBlockProperties special) return special;
		if (SIMPLE_BLOCK_MAP.containsKey(state)) return SIMPLE_BLOCK_MAP.get(state);
		if (TAG_MAP.containsKey(block)) return TAG_MAP.get(block);
		return FALLBACK_PROVIDER;
	}

}
