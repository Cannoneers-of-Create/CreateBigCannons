package rbasamoyai.createbigcannons;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.builders.FluidBuilder;
import com.simibubi.create.repack.registrate.util.entry.FluidEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullBiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CBCFluids {

	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_CAST_IRON =
			standardFluid("molten_cast_iron", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_cast_iron", "Molten Cast Iron")
			.tag(AllTags.forgeFluidTag("molten_cast_iron"))
			.attributes(b -> b.viscosity(1250)
					.density(7100)
					.temperature(1100))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> standardFluid(String name, NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> factory) {
		return CreateBigCannons.registrate()
				.fluid(name, CreateBigCannons.resource("fluid/" + name + "_still"), CreateBigCannons.resource("fluid/" + name + "_flowing"), factory);
	}
	
	public static void register() {}
	
	private static class NoColorFluidAttributes extends FluidAttributes {

		protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
			super(builder, fluid);
		}
		
		@Override
		public int getColor(BlockAndTintGetter level, BlockPos pos) {
			return 0x00ffffff;
		}		
	}
	
}
