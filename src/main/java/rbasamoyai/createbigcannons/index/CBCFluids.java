package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.ModGroup;
import rbasamoyai.createbigcannons.crafting.foundry.MoltenMetalLiquidBlock;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCFluids {

    static { ModGroup.useModTab(ModGroup.MAIN_TAB_KEY); }

	public static final RegistryEntry<CBCFlowingFluid.Flowing> MOLTEN_CAST_IRON =
			standardFluid("molten_cast_iron")
			.lang("Molten Cast Iron")
			.tag(forgeTag("molten_cast_iron"))
			.tag(fabricTag("molten_cast_iron"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
//			.attributes(b -> b.viscosity(1250)
//					.density(7100)
//					.temperature(1200))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
            .source(CBCFlowingFluid.Still::new)
			.block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.transform(IndexPlatform::doFluidBuilderTransforms)
			.register();

	public static final RegistryEntry<CBCFlowingFluid.Flowing> MOLTEN_BRONZE =
			standardFluid("molten_bronze")
			.lang("Molten Bronze")
			.tag(forgeTag("molten_bronze"))
			.tag(fabricTag("molten_bronze"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
//			.attributes(b -> b.viscosity(1250)
//					.density(8770)
//					.temperature(920))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
            .source(CBCFlowingFluid.Still::new)
            .block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.transform(IndexPlatform::doFluidBuilderTransforms)
			.register();

	public static final RegistryEntry<CBCFlowingFluid.Flowing> MOLTEN_STEEL =
			standardFluid("molten_steel")
			.lang("Molten Steel")
			.tag(forgeTag("molten_steel"))
			.tag(fabricTag("molten_steel"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
            .source(CBCFlowingFluid.Still::new)
			.block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.transform(IndexPlatform::doFluidBuilderTransforms)
			.register();

	public static final RegistryEntry<CBCFlowingFluid.Flowing> MOLTEN_NETHERSTEEL =
			standardFluid("molten_nethersteel")
			.lang("Molten Nethersteel")
			.tag(forgeTag("molten_nethersteel"))
			.tag(fabricTag("molten_nethersteel"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.properties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(3)
					.blastResistance(100f))
            .source(CBCFlowingFluid.Still::new)
			.block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.transform(IndexPlatform::doFluidBuilderTransforms)
			.register();

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.getPos().relative(pSource.getBlockState().getValue(DispenserBlock.FACING));
            Level level = pSource.getLevel();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null))
                return new ItemStack(Items.BUCKET);
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

	public static void register() {}

	private static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> createFluid(String name, NonNullFunction<CBCFlowingFluid.Properties, T> fac) {
		ResourceLocation stillTex = CreateBigCannons.resource("fluid/" + name + "_still");
		ResourceLocation flowingTex = CreateBigCannons.resource("fluid/" + name + "_flow");
		return REGISTRATE.entry(name, cb -> FluidBuilder.create(REGISTRATE, regSelf(REGISTRATE), name, cb, stillTex, flowingTex, fac));
	}

	private static <P> FluidBuilder<CBCFlowingFluid.Flowing, P> standardFluid(String name) {
		return createFluid(name, CBCFlowingFluid.Flowing::new);
	}

	@SuppressWarnings("unchecked")
	private static <S> S regSelf(AbstractRegistrate<?> reg) { return (S) reg; }

	private static TagKey<Fluid> forgeTag(String path) {
		return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CBCUtils.location("forge", path));
	}

	private static TagKey<Fluid> fabricTag(String path) {
		return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CBCUtils.location("c", path));
	}

}
