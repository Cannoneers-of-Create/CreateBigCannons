package rbasamoyai.createbigcannons.fabric.index.fluid_utils;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.fabric.FluidBlockHelper;
import com.tterrag.registrate.fabric.FluidData;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import java.util.Arrays;
import java.util.function.Supplier;

public class FabricFluidBuilder<T extends CBCFlowingFluid, P> extends FluidBuilder<T, P> {

	protected final NonNullSupplier<FluidData.Builder> attributes;
	private NonNullConsumer<FluidData.Builder> attributesCallback = $ -> {};

	public FabricFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
							  ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
		this.attributes = FluidData.Builder::new;
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

	public FabricFluidBuilder<T, P> attributes(NonNullConsumer<FluidData.Builder> cons) {
		this.attributesCallback = this.attributesCallback.andThen(cons);
		return this;
	}

	@Override
	public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
		return block1(FluidBlockHelper::createFluidBlock);
	}

	@Override
	public FluidBuilder<T, P> defaultLang() {
		return lang(f -> Util.makeDescriptionId("fluid", Registry.FLUID.getKey(f.getSource())), RegistrateLangProvider.toEnglishName(sourceName));
	}

	@Override
	public FluidBuilder<T, P> lang(String name) {
		return lang(f -> Util.makeDescriptionId("fluid", Registry.FLUID.getKey(f.getSource())), name);
	}

	@Override
	protected CBCFlowingFluid.Properties makeProperties() {
		FluidData.Builder attributes = this.attributes.get();
		RegistryEntry<Block> block = getOwner().getOptional(this.sourceName, Registry.BLOCK_REGISTRY);
		this.attributesCallback.accept(attributes);

		// Force the translation key after the user callback runs
		// This is done because we need to remove the lang data generator if using the block key,
		// and if it was possible to undo this change, it might result in the user translation getting
		// silently lost, as there's no good way to check whether the translation key was changed.
		// TODO improve this? // leaving in this TODO in case of changes in registrate's FluidBuilder -ritchie
		if (block.isPresent()) {
			attributes.translationKey(block.get().getDescriptionId());
			setData(ProviderType.LANG, NonNullBiConsumer.noop());
		} else {
			attributes.translationKey(Util.makeDescriptionId("fluid", new ResourceLocation(getOwner().getModid(), this.sourceName)));
		}

		return super.makeProperties();
	}

	@Override
	public RegistryEntry<T> register() {
		RegistryEntry<T> ret = super.register();
		if (this.renderHandler == null) {
			this.setDefaultRenderHandler();
		}
		onRegister(this::registerRenderHandler);
		return ret;
	}

	private Supplier<Supplier<RenderType>> renderLayer;
	private Supplier<RenderHandlerFactory> renderHandler;
	private int color = -1;

	public FabricFluidBuilder<T, P> layer(Supplier<Supplier<RenderType>> layer) {
		if (this.renderLayer == null) {
			onRegister(this::registerLayer);
		}
		this.renderLayer = layer;
		return this;
	}

	protected void registerLayer(T entry) {
		EnvExecute.executeOnClient(() -> () -> {
			final RenderType layer = renderLayer.get().get();
			BlockRenderLayerMap.INSTANCE.putFluid(entry, layer);
		});
	}

	public FabricFluidBuilder<T, P> renderHandler(Supplier<RenderHandlerFactory> handler) {
		if (this.color != -1) {
			throw new IllegalArgumentException("Can only set either color or render handler factory!");
		}
		this.renderHandler = handler;
		return this;
	}

	public FabricFluidBuilder<T, P> color(int color) {
		if (this.renderHandler != null) {
			throw new IllegalArgumentException("Can only set either color or render handler factory!");
		}
		this.color = color;
		return this;
	}

	protected void setDefaultRenderHandler() {
		this.renderHandler = () -> (stillTexture, flowingTexture) -> {
			final SimpleFluidRenderHandler handler = new SimpleFluidRenderHandler(stillTexture, flowingTexture, flowingTexture, this.color);
			return handler;
		};
	}

	@SuppressWarnings("deprecation")
	protected void registerRenderHandler(T entry) {
		EnvExecute.executeOnClient(() -> () -> {
			final FluidRenderHandler handler = this.renderHandler.get().create(this.stillTexture, this.flowingTexture);
			FluidRenderHandlerRegistry.INSTANCE.register(entry, handler);
			FluidRenderHandlerRegistry.INSTANCE.register(entry.getSource(), handler);
			ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) -> {
				registry.register(this.stillTexture);
				registry.register(this.flowingTexture);
			});
		});
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
		return new RegistryEntry<>(getOwner(), delegate);
	}

	public interface RenderHandlerFactory {
		FluidRenderHandler create(ResourceLocation stillTexture, ResourceLocation flowingTexture);
	}

}
