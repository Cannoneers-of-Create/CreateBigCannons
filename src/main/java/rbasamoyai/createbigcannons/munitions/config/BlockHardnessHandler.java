package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockHardnessHandler {

	public static final Map<Block, Double> TAG_MAP = new HashMap<>();
	public static final Map<Block, Double> BLOCK_MAP = new HashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() { super(GSON, "block_hardness"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			TAG_MAP.clear();
			BLOCK_MAP.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					for (Map.Entry<String, JsonElement> objEntry : el.getAsJsonObject().entrySet()) {
						JsonElement el1 = objEntry.getValue();
						if (!el1.isJsonPrimitive() || !el1.getAsJsonPrimitive().isNumber()) continue;
						String s = objEntry.getKey();
						double hardness = el1.getAsDouble();
						if (s.charAt(0) == '#') {
							s = s.substring(1);
							TagKey<Block> tag = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(s));
							for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(tag)) {
								TAG_MAP.put(holder.value(), hardness);
							}
						} else {
							String copy = s;
							Block block = Registry.BLOCK.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
								return new JsonSyntaxException("Unknown block '" + copy + "'");
							});
							BLOCK_MAP.put(block, hardness);
						}
					}
				} catch (Exception e) {

				}
			}
		}
	}

}
