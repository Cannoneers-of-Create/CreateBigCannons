package rbasamoyai.createbigcannons;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;

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
					.temperature(1200))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_BRONZE =
			standardFluid("molten_bronze", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_bronze", "Molten Bronze")
			.tag(AllTags.forgeFluidTag("molten_bronze"))
			.attributes(b -> b.viscosity(1250)
					.density(8770)
					.temperature(920))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_STEEL =
			standardFluid("molten_steel", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_steel", "Molten Steel")
			.tag(AllTags.forgeFluidTag("molten_steel"))
			.attributes(b -> b.viscosity(1250)
					.density(7040)
					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_NETHERSTEEL =
			standardFluid("molten_nethersteel", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_nethersteel", "Molten Nethersteel")
			.attributes(b -> b.viscosity(1250)
					.density(7040)
					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> standardFluid(String name, NonNullBiFunction<FluidAttributes.Builder, Fluid, FluidAttributes> factory) {
		return CreateBigCannons.registrate()
				.fluid(name, CreateBigCannons.resource("fluid/" + name + "_still"), CreateBigCannons.resource("fluid/" + name + "_flow"), factory);
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
