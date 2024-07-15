package rbasamoyai.createbigcannons.base.tag_utils;

import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;

/**
 * Copied from {@link net.fabricmc.fabric.impl.datagen.ForcedTagEntry}
 */
public class ForcedTagEntry extends TagEntry {
	private final TagEntry delegate;

	public ForcedTagEntry(TagEntry delegate) {
		super(delegate.id, true, delegate.required);
		this.delegate = delegate;
	}

	@Override
	public <T> boolean build(TagEntry.Lookup<T> lookup, Consumer<T> cons) {
		return this.delegate.build(lookup, cons);
	}

	@Override
	public boolean verifyIfPresent(Predicate<ResourceLocation> registryPredicate, Predicate<ResourceLocation> builderPredicate) {
		return true;
	}

}
