package rbasamoyai.createbigcannons.fabric.mixin.client;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.fabric.index.fluid_utils.FabricFluidBuilder;
import rbasamoyai.createbigcannons.fabric.index.fluid_utils.RenderHandlerFactory;
import rbasamoyai.createbigcannons.fabric.mixin_interface.FabricFluidBuilderClient;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

@Mixin(FabricFluidBuilder.class)
public abstract class FabricFluidBuilderClientMixin extends FluidBuilder<CBCFlowingFluid, Object> implements FabricFluidBuilderClient {

	@Unique private Supplier<Supplier<RenderType>> renderLayer;
	@Unique private Supplier<RenderHandlerFactory> renderHandler;
	@Unique private int color = -1;

	FabricFluidBuilderClientMixin(AbstractRegistrate owner, Object parent, String name, BuilderCallback callback,
								  ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
	}

	@Inject(method = "registerClient", at = @At("HEAD"), remap = false)
	private void createbigcannons$registerClient(CallbackInfo ci) {
		if (this.renderHandler != null) {
			this.renderHandler = () -> (stillTexture, flowingTexture) ->
				new SimpleFluidRenderHandler(stillTexture, flowingTexture, flowingTexture, this.color);
		}
		this.onRegister(this::registerRenderHandler);
	}

	@Inject(method = "handleClientStuff", at = @At("HEAD"), remap = false)
	private void createbigcannons$handleClientStuff(CallbackInfo ci) {
		this.renderHandler(() -> SimpleFluidRenderHandler::new);
	}

	@SuppressWarnings("deprecation")
	protected void registerRenderHandler(CBCFlowingFluid entry) {
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
	public FabricFluidBuilderClient layer(Supplier<Supplier<RenderType>> layer) {
		if (this.renderLayer == null) {
			this.onRegister(this::registerLayer);
		}
		this.renderLayer = layer;
		return this;
	}

	protected void registerLayer(CBCFlowingFluid entry) {
		EnvExecute.executeOnClient(() -> () -> {
			final RenderType layer = renderLayer.get().get();
			BlockRenderLayerMap.INSTANCE.putFluid(entry, layer);
		});
	}

	@Override
	public FabricFluidBuilderClient renderHandler(Supplier<RenderHandlerFactory> handler) {
		if (this.color != -1) {
			throw new IllegalArgumentException("Can only set either color or render handler factory!");
		}
		this.renderHandler = handler;
		return this;
	}

	@Override
	public FabricFluidBuilderClient color(int color) {
		if (this.renderHandler != null) {
			throw new IllegalArgumentException("Can only set either color or render handler factory!");
		}
		this.color = color;
		return this;
	}

}
