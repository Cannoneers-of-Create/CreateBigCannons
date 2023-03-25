package rbasamoyai.createbigcannons.forge.index.fluid_utils;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidEntry;

public class ForgeFluidBuilder<T extends CBCFlowingFluid, P> extends FluidBuilder<T, P> {

	public ForgeFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, factory);
	}

	@Override
	public BlockBuilder<LiquidBlock, FluidBuilder<T, P>> block() {
		return null;
	}

	@Override
	protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
		return new FluidEntry<>(this.getOwner(), delegate);
	}

}
