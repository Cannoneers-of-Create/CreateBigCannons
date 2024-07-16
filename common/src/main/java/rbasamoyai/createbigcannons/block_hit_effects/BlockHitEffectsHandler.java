package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.Map;

import javax.annotation.Nullable;

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

public class BlockHitEffectsHandler {

	private static final TypeAndTagDataHolder<Block, BlockHitEffect> BLOCKS = new TypeAndTagDataHolder<>(CBCRegistryUtils.getBlockRegistry());
	private static final TypeAndTagDataHolder<Block, BlockHitEffect> FLUIDS = new TypeAndTagDataHolder<>(CBCRegistryUtils.getBlockRegistry());

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener BLOCKS_INSTANCE = new ReloadListener("block", BLOCKS);
		public static final ReloadListener FLUIDS_INSTANCE = new ReloadListener("fluid", FLUIDS);

		private final TypeAndTagDataHolder<Block, BlockHitEffect> holder;

		ReloadListener(String suffix, TypeAndTagDataHolder<Block, BlockHitEffect> holder) {
			super(GSON, "block_hit_effects/" + suffix);
			this.holder = holder;
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
			this.holder.cleanUp();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ResourceLocation loc = entry.getKey();
					if (loc.getPath().startsWith("tags/")) {
						ResourceLocation pruned = CBCUtils.location(loc.getNamespace(), loc.getPath().substring(5));
						TagKey<Block> tag = TagKey.create(CBCRegistryUtils.getBlockRegistryKey(), pruned);
						this.holder.addTagData(tag, BlockHitEffect.fromJson(el.getAsJsonObject()));
					} else {
						Block block = CBCRegistryUtils.getOptionalBlock(loc).orElseThrow(() -> {
							return new JsonSyntaxException("Unknown block '" + loc + "'");
						});
						this.holder.addData(block, BlockHitEffect.fromJson(el.getAsJsonObject()));
					}
				} catch (Exception e) {
					CreateBigCannons.LOGGER.warn("Exception loading block hit effects: {}", e.getMessage());
				}
			}
			loadTags();
		}
	}

	public static void loadTags() {
		BLOCKS.loadTags();
		FLUIDS.loadTags();
	}

	public static void cleanUpTags() {
		BLOCKS.cleanUpTags();
		FLUIDS.cleanUpTags();
	}

	@Nullable public static BlockHitEffect getBlockProperties(BlockState state) { return getBlockProperties(state.getBlock()); }
	@Nullable public static BlockHitEffect getBlockProperties(Block block) { return BLOCKS.getData(block); }

	@Nullable public static BlockHitEffect getFluidProperties(BlockState state) { return getFluidProperties(state.getBlock()); }
	@Nullable public static BlockHitEffect getFluidProperties(Block block) { return FLUIDS.getData(block); }

}
