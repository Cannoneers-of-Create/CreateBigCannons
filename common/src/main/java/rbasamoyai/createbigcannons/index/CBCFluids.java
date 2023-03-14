package rbasamoyai.createbigcannons.index;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCFluids {

	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_CAST_IRON =
			REGISTRATE.standardFluid("molten_cast_iron"/*, NoColorFluidAttributes::new*/)
			.lang(f -> "fluid.createbigcannons.molten_cast_iron", "Molten Cast Iron")
			.tag(AllTags.forgeFluidTag("molten_cast_iron"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7100)
//					.temperature(1200))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Flowing::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_BRONZE =
			REGISTRATE.standardFluid("molten_bronze"/*, NoColorFluidAttributes::new*/)
			.lang(f -> "fluid.createbigcannons.molten_bronze", "Molten Bronze")
			.tag(AllTags.forgeFluidTag("molten_bronze"))
//			.attributes(b -> b.viscosity(1250)
//					.density(8770)
//					.temperature(920))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Flowing::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_STEEL =
			REGISTRATE.standardFluid("molten_steel"/*, NoColorFluidAttributes::new*/)
			.lang(f -> "fluid.createbigcannons.molten_steel", "Molten Steel")
			.tag(AllTags.forgeFluidTag("molten_steel"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Flowing::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_NETHERSTEEL =
			REGISTRATE.standardFluid("molten_nethersteel"/*, NoColorFluidAttributes::new*/)
			.lang(f -> "fluid.createbigcannons.molten_nethersteel", "Molten Nethersteel")
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Flowing::new)
			.register();
	
	public static void register() {}
	
//	private static class NoColorFluidAttributes extends FluidVariantAttributes {
//
//		protected NoColorFluidAttributes(Builder builder, Fluid fluid) {
//			super(builder, fluid);
//		}
//
//		@Override
//		public int getColor(BlockAndTintGetter level, BlockPos pos) {
//			return 0x00ffffff;
//		}
//	}
	
}
