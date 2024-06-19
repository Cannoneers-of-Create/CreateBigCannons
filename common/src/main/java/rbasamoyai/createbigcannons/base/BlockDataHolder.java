package rbasamoyai.createbigcannons.base;

import java.util.Map;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class BlockDataHolder<T> {
	private final Map<Block, T> tagData = new Reference2ObjectOpenHashMap<>();
	private final Map<Block, T> blockData = new Reference2ObjectOpenHashMap<>();
	private final Map<TagKey<Block>, T> tagsToEvaluate = new Object2ObjectLinkedOpenHashMap<>();

	public void addTagData(TagKey<Block> tag, T effect) {
		this.tagsToEvaluate.put(tag, effect);
	}

	public void addBlockData(Block block, T effect) {
		this.blockData.put(block, effect);
	}

	public void cleanUp() {
		this.tagData.clear();
		this.blockData.clear();
		this.tagsToEvaluate.clear();
	}

	public void cleanUpTags() {
		this.tagData.clear();
	}

	/**
	 * Does not clear tags, used for tag-dependent client resources
	 */
	public void loadTags() { this.loadTags(false); }

	public void loadTags(boolean clearTagsToEvalulate) {
		this.tagData.clear();
		for (Map.Entry<TagKey<Block>, T> entry : this.tagsToEvaluate.entrySet()) {
			T properties = entry.getValue();
			for (Holder<Block> holder : CBCRegistryUtils.getBlockTagEntries(entry.getKey()))
				this.tagData.put(holder.value(), properties);
		}
		if (clearTagsToEvalulate)
			this.tagsToEvaluate.clear();
	}

	@Nullable
	public T getData(Block block) {
		if (this.blockData.containsKey(block)) return this.blockData.get(block);
		if (this.tagData.containsKey(block)) return this.tagData.get(block);
		return null;
	}
}
