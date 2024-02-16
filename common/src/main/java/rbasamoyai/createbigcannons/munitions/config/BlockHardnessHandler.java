package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockHardnessHandler {

	public static final Map<Block, Double> TAG_MAP = new HashMap<>();
	public static final Map<Block, Double> BLOCK_MAP = new HashMap<>();
	public static final Map<TagKey<Block>, Double> TAGS_TO_EVALUATE = new LinkedHashMap<>();

	public static class BlockReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final BlockReloadListener INSTANCE = new BlockReloadListener();

		public BlockReloadListener() { super(GSON, "block_hardness"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					double hardness = GsonHelper.getAsDouble(el.getAsJsonObject(), "block_hardness");
					ResourceLocation loc = entry.getKey();
					if (loc.getPath().startsWith("tags/")) {
						ResourceLocation pruned = new ResourceLocation(loc.getNamespace(), loc.getPath().substring(5));
						TagKey<Block> tag = TagKey.create(Registry.BLOCK_REGISTRY, pruned);
						TAGS_TO_EVALUATE.put(tag, hardness);
					} else {
						Block block = Registry.BLOCK.getOptional(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						BLOCK_MAP.put(block, hardness);
					}
				} catch (Exception e) {

				}
			}
		}
	}

	public static void loadTags() {
		TAG_MAP.clear();
		for (Map.Entry<TagKey<Block>, Double> entry : TAGS_TO_EVALUATE.entrySet()) {
			double hardness = entry.getValue();
			for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(entry.getKey())) {
				TAG_MAP.put(holder.value(), hardness);
			}
		}
		TAGS_TO_EVALUATE.clear();
	}

	public static void cleanUp() {
		TAG_MAP.clear();
		BLOCK_MAP.clear();
		TAGS_TO_EVALUATE.clear();
	}

	@Deprecated
	public static double getHardness(BlockState state) {
		Block block = state.getBlock();
		if (BLOCK_MAP.containsKey(block)) return BLOCK_MAP.get(block);
		if (TAG_MAP.containsKey(block)) return TAG_MAP.get(block);
		return block.getExplosionResistance();
	}

	public static double getHardness(BlockState state, Level level, BlockPos pos) {
		Block block = state.getBlock();
		if (block instanceof PropertiesMimicTypeBlock m) block = m.createBigCannons$getActualBlock(state, level, pos);
		if (BLOCK_MAP.containsKey(block)) return BLOCK_MAP.get(block);
		if (TAG_MAP.containsKey(block)) return TAG_MAP.get(block);
		return block.getExplosionResistance();
	}
}
