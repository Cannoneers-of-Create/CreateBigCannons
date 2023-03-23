package rbasamoyai.createbigcannons.index;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.MoltenMetalLiquidBlock;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidEntry;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCFluids {

	public static final FluidEntry<CBCFlowingFluid.Flowing> MOLTEN_CAST_IRON =
			standardFluid("molten_cast_iron")
			.lang(f -> "fluid.createbigcannons.molten_cast_iron", "Molten Cast Iron")
			.tag(AllTags.forgeFluidTag("molten_cast_iron"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7100)
//					.temperature(1200))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(CBCFlowingFluid.Flowing::new)
			.block1(MoltenMetalLiquidBlock::new).build()
			.register();
	
	public static final FluidEntry<CBCFlowingFluid.Flowing> MOLTEN_BRONZE =
			standardFluid("molten_bronze")
			.lang(f -> "fluid.createbigcannons.molten_bronze", "Molten Bronze")
			.tag(AllTags.forgeFluidTag("molten_bronze"))
//			.attributes(b -> b.viscosity(1250)
//					.density(8770)
//					.temperature(920))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(CBCFlowingFluid.Flowing::new)
			.block1(MoltenMetalLiquidBlock::new).build()
			.register();
	
	public static final FluidEntry<CBCFlowingFluid.Flowing> MOLTEN_STEEL =
			standardFluid("molten_steel")
			.lang(f -> "fluid.createbigcannons.molten_steel", "Molten Steel")
			.tag(AllTags.forgeFluidTag("molten_steel"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(CBCFlowingFluid.Flowing::new)
			.block1(MoltenMetalLiquidBlock::new).build()
			.register();
	
	public static final FluidEntry<CBCFlowingFluid.Flowing> MOLTEN_NETHERSTEEL =
			standardFluid("molten_nethersteel")
			.lang(f -> "fluid.createbigcannons.molten_nethersteel", "Molten Nethersteel")
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
			.source(CBCFlowingFluid.Flowing::new)
			.block1(MoltenMetalLiquidBlock::new).build()
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

	private static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> createFluid(String name, NonNullFunction<CBCFlowingFluid.Properties, T> fac) {
		ResourceLocation stillTex = CreateBigCannons.resource("fluid/" + name + "_still");
		ResourceLocation flowingTex = CreateBigCannons.resource("fluid/" + name + "_flow");
		return REGISTRATE.entry(name, cb -> FluidBuilder.create(REGISTRATE, regSelf(REGISTRATE), name, cb, stillTex, flowingTex, fac));
	}

	private static <P> FluidBuilder<CBCFlowingFluid.Flowing, P> standardFluid(String name) {
		return createFluid(name, CBCFlowingFluid.Flowing::new);
	}

	@SuppressWarnings("unchecked")
	private static <S extends AbstractRegistrate<S>> S regSelf(AbstractRegistrate<?> reg) { return (S) reg; }

}
