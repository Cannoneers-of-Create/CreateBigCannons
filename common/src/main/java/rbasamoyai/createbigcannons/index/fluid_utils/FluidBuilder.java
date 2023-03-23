package rbasamoyai.createbigcannons.index.fluid_utils;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.fabric.FluidBlockHelper;
import com.tterrag.registrate.fabric.FluidData;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.*;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.base.LazySupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copy of {@link com.tterrag.registrate.builders.FluidBuilder} to work with multiloader fluid impl
 */
public class FluidBuilder<T extends CBCFlowingFluid, P> extends AbstractBuilder<Fluid, T, P, FluidBuilder<T, P>> {
	
	public static <P> FluidBuilder<CBCFlowingFluid.Flowing, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
		return create(owner, parent, name, callback, stillTexture, flowingTexture, CBCFlowingFluid.Flowing::new);
	}
	
	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
			NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		FluidBuilder<T, P> ret = new FluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture, factory)
				.defaultLang().defaultSource().defaultBlock().defaultBucket()
				.tag(FluidTags.WATER);

		return ret;
	}

	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final String sourceName;
	private final String bucketName;
	private final NonNullSupplier<FluidData.Builder> attributes;
	private final NonNullFunction<CBCFlowingFluid.Properties, T> factory;

	@Nullable
	private Boolean defaultSource, defaultBlock, defaultBucket;

	private NonNullConsumer<FluidData.Builder> attributesCallback = $ -> {};
	private NonNullConsumer<CBCFlowingFluid.Properties> properties;
	@Nullable
	private NonNullSupplier<? extends CBCFlowingFluid> source;
	private List<TagKey<Fluid>> tags = new ArrayList<>();

	protected FluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
						   NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		super(owner, parent, "flowing_" + name, callback, Registry.FLUID_REGISTRY);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.sourceName = name;
		this.bucketName = name + "_bucket";
		this.attributes = FluidData.Builder::new;
		this.factory = factory;

		String bucketName = this.bucketName;
		this.properties = p -> p.bucket(() -> owner.get(bucketName, Registry.ITEM_REGISTRY).get())
				.block(() -> owner.<Block, LiquidBlock>get(name, Registry.BLOCK_REGISTRY).get());
	}

	public FluidBuilder<T, P> attributes(NonNullConsumer<FluidData.Builder> cons) {
		attributesCallback = attributesCallback.andThen(cons);
		return this;
	}
	
	public FluidBuilder<T, P> properties(NonNullConsumer<CBCFlowingFluid.Properties> cons) {
		properties = properties.andThen(cons);
		return this;
	}

	public FluidBuilder<T, P> defaultLang() {
		return lang(f -> Util.makeDescriptionId("fluid", Registry.FLUID.getKey(f.getSource())), RegistrateLangProvider.toEnglishName(sourceName));
	}

	public FluidBuilder<T, P> lang(String name) {
		return lang(f -> Util.makeDescriptionId("fluid", Registry.FLUID.getKey(f.getSource())), name);
	}

	public FluidBuilder<T, P> defaultSource() {
		if (this.defaultSource != null) {
			throw new IllegalStateException("Cannot set a default source after a custom source has been created");
		}
		this.defaultSource = true;
		return this;
	}

	public FluidBuilder<T, P> source(NonNullFunction<CBCFlowingFluid.Properties, ? extends CBCFlowingFluid> factory) {
		this.defaultSource = false;
		this.source = new LazySupplier<>(() -> factory.apply(makeProperties()));
		return this;
	}

	public FluidBuilder<T, P> defaultBlock() {
		if (this.defaultBlock != null) {
			throw new IllegalStateException("Cannot set a default block after a custom block has been created");
		}
		this.defaultBlock = true;
		return this;
	}

	public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
		return block1(FluidBlockHelper::createFluidBlock);
	}

	public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block(NonNullBiFunction<NonNullSupplier<? extends T>, BlockBehaviour.Properties, ? extends B> factory) {
		if (this.defaultBlock == Boolean.FALSE) {
			throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
		}
		this.defaultBlock = false;
		NonNullSupplier<T> supplier = asSupplier();
		return getOwner().<B, FluidBuilder<T, P>>block(this, sourceName, p -> factory.apply(supplier, p))
				.properties(p -> BlockBehaviour.Properties.copy(Blocks.WATER).noDrops())
				.blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getBuilder(sourceName)
						.texture("particle", stillTexture)));
	}

	@SuppressWarnings("unchecked")
	public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block1(NonNullBiFunction<? extends T, BlockBehaviour.Properties, ? extends B> factory) {
		return block((supplier, settings) -> ((NonNullBiFunction<T, BlockBehaviour.Properties, ? extends B>) factory).apply(supplier.get(), settings));
	}

	@Beta
	public FluidBuilder<T, P> noBlock() {
		if (this.defaultBlock == Boolean.FALSE) {
			throw new IllegalStateException("Only one call to block/noBlock per builder allowed");
		}
		this.defaultBlock = false;
		return this;
	}

	public FluidBuilder<T, P> defaultBucket() {
		if (this.defaultBucket != null) {
			throw new IllegalStateException("Cannot set a default bucket after a custom bucket has been created");
		}
		defaultBucket = true;
		return this;
	}

	public ItemBuilder<BucketItem, FluidBuilder<T, P>> bucket() {
		return bucket(BucketItem::new);
	}

	public <I extends BucketItem> ItemBuilder<I, FluidBuilder<T, P>> bucket(NonNullBiFunction<? extends CBCFlowingFluid, Item.Properties, ? extends I> factory) {
		if (this.defaultBucket == Boolean.FALSE) {
			throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
		}
		this.defaultBucket = false;
		NonNullSupplier<? extends CBCFlowingFluid> source = this.source;
		if (source == null) {
			throw new IllegalStateException("Cannot create a bucket before creating a source block");
		}
		return getOwner().<I, FluidBuilder<T, P>>item(this, bucketName, p -> ((NonNullBiFunction<CBCFlowingFluid, Item.Properties, ? extends I>) factory).apply(this.source.get(), p)) // Fabric TODO
				.properties(p -> p.craftRemainder(Items.BUCKET).stacksTo(1))
				.model((ctx, prov) -> prov.generated(ctx::getEntry, new ResourceLocation(getOwner().getModid(), "item/" + bucketName)));
	}

	@Beta
	public FluidBuilder<T, P> noBucket() {
		if (this.defaultBucket == Boolean.FALSE) {
			throw new IllegalStateException("Only one call to bucket/noBucket per builder allowed");
		}
		this.defaultBucket = false;
		return this;
	}

	@SafeVarargs
	public final FluidBuilder<T, P> tag(TagKey<Fluid>... tags) {
		FluidBuilder<T, P> ret = this.tag(ProviderType.FLUID_TAGS, tags);
		if (this.tags.isEmpty()) {
			ret.getOwner().<RegistrateTagsProvider<Fluid>, Fluid>setDataGenerator(ret.sourceName, getRegistryKey(), ProviderType.FLUID_TAGS,
					prov -> this.tags.stream().map(prov::tag).forEach(p -> p.add(this.getSource())));
		}
		this.tags.addAll(Arrays.asList(tags));
		return ret;
	}

	@SafeVarargs
	public final FluidBuilder<T, P> removeTag(TagKey<Fluid>... tags) {
		this.tags.removeAll(Arrays.asList(tags));
		return this.removeTag(ProviderType.FLUID_TAGS, tags);
	}

	private CBCFlowingFluid getSource() {
		NonNullSupplier<? extends CBCFlowingFluid> source = this.source;
		Preconditions.checkNotNull(source, "Fluid has no source block: " + sourceName);
		return source.get();
	}

	private CBCFlowingFluid.Properties makeProperties() {
		FluidData.Builder attributes = this.attributes.get();
		RegistryEntry<Block> block = getOwner().getOptional(sourceName, Registry.BLOCK_REGISTRY);
		attributesCallback.accept(attributes);
		// Force the translation key after the user callback runs
		// This is done because we need to remove the lang data generator if using the block key,
		// and if it was possible to undo this change, it might result in the user translation getting
		// silently lost, as there's no good way to check whether the translation key was changed.
		// TODO improve this? // leaving in this TODO in case of changes in registrate's FluidBuilder -ritchie
		if (block.isPresent()) {
			attributes.translationKey(block.get().getDescriptionId());
			setData(ProviderType.LANG, NonNullBiConsumer.noop());
		} else {
			attributes.translationKey(Util.makeDescriptionId("fluid", new ResourceLocation(getOwner().getModid(), sourceName)));
		}
		NonNullSupplier<? extends CBCFlowingFluid> source = this.source;
		CBCFlowingFluid.Properties ret = new CBCFlowingFluid.Properties(source == null ? null : source::get, asSupplier(), attributes);
		properties.accept(ret);
		return ret;
	}

	@Override
	protected T createEntry() {
		return factory.apply(makeProperties());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public FluidEntry<T> register() {
		if (defaultSource == Boolean.TRUE) {
			source(CBCFlowingFluid.Still::new);
		}
		if (defaultBlock == Boolean.TRUE) {
			block().register();
		}
		if (defaultBucket == Boolean.TRUE) {
			bucket().register();
		}
		NonNullSupplier<? extends CBCFlowingFluid> source = this.source;
		if (source != null) {
			getCallback().accept(sourceName, Fluid.class, (FluidBuilder) this, source::get);
		} else {
			throw new IllegalStateException("Fluid must have a source version: " + getName());
		}
//		if (renderHandler == null) {
//			this.setDefaultRenderHandler();
//		}
//		onRegister(this::registerRenderHandler);
		return (FluidEntry<T>) super.register();
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) { // TODO: abstract and make impls
		return new FluidEntry<>(getOwner(), delegate);
	}

	// Fabric stuffs, nothing called if platform is forge

	public FluidBuilder<T, P> renderSprite() {
		return this;
	}

//	@SuppressWarnings("deprecation")
//	protected void registerRenderHandler(T entry) {
//		EnvExecute.executeOnClient(() -> () -> {
//			final FluidRenderHandler handler = this.renderHandler.get().create(this.stillTexture, this.flowingTexture);
//			FluidRenderHandlerRegistry.INSTANCE.register(entry, handler);
//			FluidRenderHandlerRegistry.INSTANCE.register(entry.getSource(), handler);
//			ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
//				registry.register(stillTexture);
//				registry.register(flowingTexture);
//			});
//		});
//	}

}

