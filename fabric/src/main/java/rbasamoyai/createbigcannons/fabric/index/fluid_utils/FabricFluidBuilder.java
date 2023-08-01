package rbasamoyai.createbigcannons.fabric.index.fluid_utils;

import java.util.Arrays;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.mixin.accessor.FluidBlockAccessor;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;

public class FabricFluidBuilder<T extends CBCFlowingFluid, P> extends FluidBuilder<T, P> {

	protected final NonNullSupplier<CBCFluidData.Builder> attributes;
	private NonNullConsumer<CBCFluidData.Builder> attributesCallback = $ -> {};

	public FabricFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
							  ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
		this.attributes = CBCFluidData.Builder::new;
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

	public FabricFluidBuilder<T, P> attributes(NonNullConsumer<CBCFluidData.Builder> cons) {
		this.attributesCallback = this.attributesCallback.andThen(cons);
		return this;
	}

	@Override
	public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
		return block1(FluidBlockAccessor::callInit);
	}

	@SuppressWarnings("unchecked")
	public <B extends LiquidBlock> BlockBuilder<B, FluidBuilder<T, P>> block1(NonNullBiFunction<? extends T, BlockBehaviour.Properties, ? extends B> factory) {
		return block((supplier, settings) -> ((NonNullBiFunction<T, BlockBehaviour.Properties, ? extends B>) factory).apply(supplier.get(), settings));
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
		CBCFluidData.Builder attributes = this.attributes.get();
		RegistryEntry<Block> block = getOwner().getOptional(this.sourceName, Registry.BLOCK_REGISTRY);
		this.attributesCallback.accept(attributes);
		attributes.translationKey(Util.makeDescriptionId("fluid", new ResourceLocation(getOwner().getModid(), this.sourceName)));
		return super.makeProperties();
	}

	@Override
	public RegistryEntry<T> register() {
		RegistryEntry<T> ret = super.register();
		this.registerClient();
		return ret;
	}

	public void registerClient() {
	}

	public void handleClientStuff() {
	}

}
