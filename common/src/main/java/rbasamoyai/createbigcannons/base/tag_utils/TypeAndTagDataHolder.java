package rbasamoyai.createbigcannons.base.tag_utils;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TypeAndTagDataHolder<TYPE, VALUE> {
	protected final Map<TYPE, VALUE> tagData = new Reference2ObjectOpenHashMap<>();
	protected final Map<TYPE, VALUE> typeData = new Reference2ObjectOpenHashMap<>();
	protected final Map<TagKey<TYPE>, VALUE> tagsToEvaluate = new Object2ObjectLinkedOpenHashMap<>();

	protected final Registry<TYPE> registry;

	public TypeAndTagDataHolder(Registry<TYPE> registry) {
		this.registry = registry;
	}

	public void addTagData(TagKey<TYPE> tag, VALUE value) {
		this.tagsToEvaluate.put(tag, value);
	}

	public void addData(TYPE type, VALUE value) {
		this.typeData.put(type, value);
	}

	public void cleanUp() {
		this.tagData.clear();
		this.typeData.clear();
		this.tagsToEvaluate.clear();
	}

	public void cleanUpTags() {
		this.tagData.clear();
	}

	public void loadTags() {
		this.tagData.clear();
		for (Map.Entry<TagKey<TYPE>, VALUE> entry : this.tagsToEvaluate.entrySet()) {
			VALUE properties = entry.getValue();
			for (Holder<TYPE> holder : this.registry.getTagOrEmpty(entry.getKey()))
				this.tagData.put(holder.value(), properties);
		}
	}

	@Nullable
	public VALUE getData(TYPE type) {
		if (this.typeData.containsKey(type)) return this.typeData.get(type);
		if (this.tagData.containsKey(type)) return this.tagData.get(type);
		return null;
	}

	public void writeToNetwork(FriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, VALUE> consumer) {
		buf.writeVarInt(this.typeData.size());
		for (Map.Entry<TYPE, VALUE> entry : this.typeData.entrySet()) {
			ResourceLocation key = this.registry.getKey(entry.getKey());
			buf.writeBoolean(key != null);
			if (key == null)
				continue;
			buf.writeResourceLocation(key);
			consumer.accept(buf, entry.getValue());
		}
		buf.writeVarInt(this.tagData.size());
		for (Map.Entry<TYPE, VALUE> entry : this.tagData.entrySet()) {
			ResourceLocation key = this.registry.getKey(entry.getKey());
			buf.writeBoolean(key != null);
			if (key == null)
				continue;
			buf.writeResourceLocation(key);
			consumer.accept(buf, entry.getValue());
		}
	}

	public void readFromNetwork(FriendlyByteBuf buf, Function<FriendlyByteBuf, VALUE> consumer) {
		this.cleanUp();
		int typeSize = buf.readVarInt();
		for (int i = 0; i < typeSize; ++i) {
			if (!buf.readBoolean())
				continue;
			ResourceLocation location = buf.readResourceLocation();
			TYPE type = this.registry.get(location);
			VALUE value = consumer.apply(buf);
			if (type != null)
				this.typeData.put(type, value);
		}
		int tagSize = buf.readVarInt();
		for (int i = 0; i < tagSize; ++i) {
			if (!buf.readBoolean())
				continue;
			ResourceLocation location = buf.readResourceLocation();
			TYPE type = this.registry.get(location);
			VALUE value = consumer.apply(buf);
			if (type != null)
				this.tagData.put(type, value);
		}
	}

}
