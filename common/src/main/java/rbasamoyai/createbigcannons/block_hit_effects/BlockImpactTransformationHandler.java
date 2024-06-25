package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.tag_utils.TypeAndTagDataHolder;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class BlockImpactTransformationHandler {

	private static final TypeAndTagDataHolder<Block, BlockTransformation> TRANSFORMS = new TypeAndTagDataHolder<>(CBCRegistryUtils.getBlockRegistry());

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		ReloadListener() { super(GSON, "block_impact_transforms"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			TRANSFORMS.cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					if (loc.getPath().startsWith("tags/")) {
						ResourceLocation pruned = CBCUtils.location(loc.getNamespace(), loc.getPath().substring(5));
						TagKey<Block> tag = TagKey.create(CBCRegistryUtils.getBlockRegistryKey(), pruned);
						TRANSFORMS.addTagData(tag, BlockTransformation.fromJson(el.getAsJsonObject()));
					} else {
						Block block = CBCRegistryUtils.getOptionalBlock(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						TRANSFORMS.addData(block, BlockTransformation.fromJson(el.getAsJsonObject()));
					}
				} catch (Exception e) {
					CreateBigCannons.LOGGER.warn("Exception loading block impact transforms: {}", e.getMessage());
				}
			}
		}
	}

	public static void loadTags() { TRANSFORMS.loadTags(); }

	public static BlockState transformBlock(BlockState blockState) {
		BlockTransformation transform = TRANSFORMS.getData(blockState.getBlock());
		return transform == null ? blockState : transform.transformBlock(blockState);
	}

}
