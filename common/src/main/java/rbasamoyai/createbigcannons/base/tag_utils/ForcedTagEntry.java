package rbasamoyai.createbigcannons.base.tag_utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gson.JsonArray;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;

/**
 * Copied from {@link net.fabricmc.fabric.impl.datagen.ForcedTagEntry}
 *
 * @param delegate
 */
public record ForcedTagEntry(Tag.Entry delegate) implements Tag.Entry {
	@Override
	public <T> boolean build(Function<ResourceLocation, Tag<T>> tagFunc, Function<ResourceLocation, T> valueFunc, Consumer<T> cons) {
		return this.delegate.build(tagFunc, valueFunc, cons);
	}

	@Override public void serializeTo(JsonArray json) { this.delegate.serializeTo(json); }

	@Override
	public boolean verifyIfPresent(Predicate<ResourceLocation> registryPredicate, Predicate<ResourceLocation> builderPredicate) {
		return true;
	}

}
