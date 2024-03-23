package rbasamoyai.createbigcannons.block_armor_properties;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
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

public class BlockArmorPropertiesHandler {

	private static final Map<Block, BlockArmorPropertiesProvider> TAG_MAP = new Reference2ObjectOpenHashMap<>();
	private static final Map<Block, BlockArmorPropertiesProvider> BLOCK_MAP = new Reference2ObjectOpenHashMap<>();
	private static final Map<TagKey<Block>, BlockArmorPropertiesProvider> TAGS_TO_EVALUATE = new Reference2ObjectLinkedOpenHashMap<>();

	private static final Map<Block, BlockArmorPropertiesSerializer> CUSTOM_SERIALIZERS = new Reference2ObjectOpenHashMap<>();

	private static final BlockArmorPropertiesProvider FALLBACK_PROVIDER = new BlockArmorPropertiesProvider() {
		@Override public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) { return state.getBlock().getExplosionResistance(); }
	};

	public static class BlockReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final BlockReloadListener INSTANCE = new BlockReloadListener();

		public BlockReloadListener() { super(GSON, "block_armor"); }

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
						TAGS_TO_EVALUATE.put(tag, SimpleBlockArmorProperties.fromJson("#" + loc, el.getAsJsonObject()));
					} else {
						Block block = Registry.BLOCK.getOptional(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						if (CUSTOM_SERIALIZERS.containsKey(block)) {
							BLOCK_MAP.put(block, CUSTOM_SERIALIZERS.get(block).loadBlockArmorPropertiesFromJson(block, loc.toString(), el.getAsJsonObject()));
						} else {
							BLOCK_MAP.put(block, VariantBlockArmorProperties.fromJson(block, loc.toString(), el.getAsJsonObject()));
						}
					}
				} catch (Exception e) {
					//CreateBigCannons.LOGGER.warn("Exception loading block armor properties: {}", e.getMessage());
					//Commented out for silent "optional" loading if a bit silly
				}
			}
		}
	}

	public static void onDataPackReload() {
		TAG_MAP.clear();
		for (Map.Entry<TagKey<Block>, BlockArmorPropertiesProvider> entry : TAGS_TO_EVALUATE.entrySet()) {
			BlockArmorPropertiesProvider properties = entry.getValue();
			for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(entry.getKey())) {
				TAG_MAP.put(holder.value(), properties);
			}
		}
		TAGS_TO_EVALUATE.clear();
	}

	public static void cleanUp() {
		TAG_MAP.clear();
		BLOCK_MAP.clear();
		TAGS_TO_EVALUATE.clear();
	}

	public static BlockArmorPropertiesProvider getProperties(BlockState state) { return getProperties(state.getBlock()); }

	public static BlockArmorPropertiesProvider getProperties(Block block) {
		if (BLOCK_MAP.containsKey(block)) return BLOCK_MAP.get(block);
		if (TAG_MAP.containsKey(block)) return TAG_MAP.get(block);
		return FALLBACK_PROVIDER;
	}

	public static <T extends BlockArmorPropertiesSerializer> T registerCustomSerializer(Block block, T ser) {
		if (CUSTOM_SERIALIZERS.containsKey(block)) {
			throw new IllegalStateException("Serializer for block " + Registry.BLOCK.getKey(block) + " already registered");
		}
		CUSTOM_SERIALIZERS.put(block, ser);
		return ser;
	}

}
