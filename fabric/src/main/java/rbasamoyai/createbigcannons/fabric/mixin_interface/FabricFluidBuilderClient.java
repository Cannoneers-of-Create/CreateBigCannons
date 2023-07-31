package rbasamoyai.createbigcannons.fabric.mixin_interface;

import net.minecraft.client.renderer.RenderType;
import rbasamoyai.createbigcannons.fabric.index.fluid_utils.RenderHandlerFactory;

import java.util.function.Supplier;

public interface FabricFluidBuilderClient {

	FabricFluidBuilderClient layer(Supplier<Supplier<RenderType>> layer);
	FabricFluidBuilderClient renderHandler(Supplier<RenderHandlerFactory> handler);
	FabricFluidBuilderClient color(int color);

}
