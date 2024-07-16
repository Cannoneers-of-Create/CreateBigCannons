package rbasamoyai.createbigcannons.base.tag_utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class MultiEntryTypeAndTagDataHolder<TYPE, VALUE> extends TypeAndTagDataHolder<TYPE, List<VALUE>> {

	public MultiEntryTypeAndTagDataHolder(Registry<TYPE> registry) {
		super(registry);
	}

	/**
	 * Use {@link #addDataToList(Object, Object)}
	 */
	@Deprecated
	@Override
	public void addData(TYPE type, List<VALUE> value) {}

	/**
	 * Use {@link #addTagDataToList(TagKey, Object)}
	 */
	@Deprecated
	@Override
	public void addTagData(TagKey<TYPE> tag, List<VALUE> value) {}

	public void addDataToList(TYPE type, VALUE value) {
		if (!this.typeData.containsKey(type))
			this.typeData.put(type, new LinkedList<>());
		List<VALUE> list = this.typeData.get(type);
		list.add(value);
	}

	public void addTagDataToList(TagKey<TYPE> tag, VALUE value) {
		if (!this.tagsToEvaluate.containsKey(tag))
			this.tagsToEvaluate.put(tag, new LinkedList<>());
		List<VALUE> list = this.tagsToEvaluate.get(tag);
		list.add(value);
	}

	@Nonnull
	@Override
	public List<VALUE> getData(TYPE type) {
		List<VALUE> data = super.getData(type);
		return data == null ? List.of() : data;
	}

	/**
	 *
	 */
	@Deprecated
	@Override
	public void writeToNetwork(FriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, List<VALUE>> consumer) {
	}

	/**
	 *
	 */
	@Deprecated
	@Override
	public void readFromNetwork(FriendlyByteBuf buf, Function<FriendlyByteBuf, List<VALUE>> consumer) {
	}

	public void writeListsToNetwork(FriendlyByteBuf buf, BiConsumer<FriendlyByteBuf, VALUE> consumer) {
		buf.writeVarInt(this.typeData.size());
		for (Map.Entry<TYPE, List<VALUE>> entry : this.typeData.entrySet()) {
			ResourceLocation key = this.registry.getKey(entry.getKey());
			buf.writeBoolean(key != null);
			if (key == null)
				continue;
			buf.writeResourceLocation(key);
			List<VALUE> list = entry.getValue();
			buf.writeVarInt(list.size());
			for (VALUE value : list)
				consumer.accept(buf, value);
		}
		buf.writeVarInt(this.tagData.size());
		for (Map.Entry<TYPE, List<VALUE>> entry : this.tagData.entrySet()) {
			ResourceLocation key = this.registry.getKey(entry.getKey());
			buf.writeBoolean(key != null);
			if (key == null)
				continue;
			buf.writeResourceLocation(key);
			List<VALUE> list = entry.getValue();
			buf.writeVarInt(list.size());
			for (VALUE value : list)
				consumer.accept(buf, value);
		}
	}

	public void readListsFromNetwork(FriendlyByteBuf buf, Function<FriendlyByteBuf, VALUE> consumer) {
		this.cleanUp();
		int typeSize = buf.readVarInt();
		for (int i = 0; i < typeSize; ++i) {
			if (!buf.readBoolean())
				continue;
			ResourceLocation location = buf.readResourceLocation();
			TYPE type = this.registry.get(location);
			int size = buf.readVarInt();
			for (int j = 0; j < size; ++j) {
				VALUE value = consumer.apply(buf);
				if (type == null)
					continue;
				if (!this.typeData.containsKey(type))
					this.typeData.put(type, new LinkedList<>());
				List<VALUE> list = this.typeData.get(type);
				list.add(value);
			}
		}
		int tagSize = buf.readVarInt();
		for (int i = 0; i < tagSize; ++i) {
			if (!buf.readBoolean())
				continue;
			ResourceLocation location = buf.readResourceLocation();
			TYPE type = this.registry.get(location);
			int size = buf.readVarInt();
			for (int j = 0; j < size; ++j) {
				VALUE value = consumer.apply(buf);
				if (type == null)
					continue;
				if (!this.tagData.containsKey(type))
					this.tagData.put(type, new LinkedList<>());
				List<VALUE> list = this.tagData.get(type);
				list.add(value);
			}
		}
	}

}
