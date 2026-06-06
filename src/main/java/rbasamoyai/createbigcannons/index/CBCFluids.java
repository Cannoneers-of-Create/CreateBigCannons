package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Supplier;

import org.joml.Vector3f;

import com.simibubi.create.AllFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.ModGroup;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.foundry.MoltenMetalLiquidBlock;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCFluids {

    static {
        ModGroup.setDefaultTabToNull();
        REGISTRATE.setCreativeTab(null);
    }

	public static final RegistryEntry<Fluid, BaseFlowingFluid.Flowing> MOLTEN_CAST_IRON = REGISTRATE
        .standardFluid("molten_cast_iron", SolidRenderedPlaceableFluidType.create(0x460A0B,
            () -> 1f / 32f * CBCConfigs.client().moltenMetalTransparencyMultiplier.getF()))
			.lang("Molten Cast Iron")
			.tag(commonTag("molten_cast_iron"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
			.properties(b -> b.viscosity(1250)
				.density(7100)
				.temperature(1200))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                .tickRate(25)
                .slopeFindDistance(3)
                .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.register();

	public static final RegistryEntry<Fluid, BaseFlowingFluid.Flowing> MOLTEN_BRONZE = REGISTRATE
        .standardFluid("molten_bronze", SolidRenderedPlaceableFluidType.create(0x634216,
            () -> 1f / 32f * CBCConfigs.client().moltenMetalTransparencyMultiplier.getF()))
			.lang("Molten Bronze")
			.tag(commonTag("molten_bronze"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
			.properties(b -> b.viscosity(1250)
				.density(8770)
				.temperature(920))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                .tickRate(25)
                .slopeFindDistance(3)
                .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.register();

	public static final RegistryEntry<Fluid, BaseFlowingFluid.Flowing> MOLTEN_STEEL = REGISTRATE
        .standardFluid("molten_steel", SolidRenderedPlaceableFluidType.create(0x6F6E6A,
            () -> 1f / 32f * CBCConfigs.client().moltenMetalTransparencyMultiplier.getF()))
			.lang("Molten Steel")
			.tag(commonTag("molten_steel"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
			.properties(b -> b.viscosity(1250)
                .density(7040)
				.temperature(1430))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                .tickRate(25)
                .slopeFindDistance(3)
                .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.register();

	public static final RegistryEntry<Fluid, BaseFlowingFluid.Flowing> MOLTEN_NETHERSTEEL = REGISTRATE
        .standardFluid("molten_nethersteel", SolidRenderedPlaceableFluidType.create(0x4C323A,
            () -> 1f / 32f * CBCConfigs.client().moltenMetalTransparencyMultiplier.getF()))
			.lang("Molten Nethersteel")
			.tag(commonTag("molten_nethersteel"))
			.tag(CBCTags.CBCFluidTags.MOLTEN_METAL)
			.properties(b -> b.viscosity(1250)
				.density(7040)
				.temperature(1430))
            .fluidProperties(p -> p.levelDecreasePerBlock(2)
                .tickRate(25)
                .slopeFindDistance(3)
                .explosionResistance(100f))
            .source(BaseFlowingFluid.Source::new)
            .block(MoltenMetalLiquidBlock::new).build()
            .bucket()
            .onRegister(CBCFluids::registerFluidDispenseBehavior)
            .build()
			.register();

    private static final DispenseItemBehavior DEFAULT = new DefaultDispenseItemBehavior();
    private static final DispenseItemBehavior DISPENSE_FLUID = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) pStack.getItem();
            BlockPos pos = pSource.pos().relative(pSource.state().getValue(DispenserBlock.FACING));
            Level level = pSource.level();
            if (dispensibleContainerItem.emptyContents(null, level, pos, null))
                return new ItemStack(Items.BUCKET);
            return DEFAULT.dispense(pSource, pStack);
        }
    };

    private static void registerFluidDispenseBehavior(BucketItem bucket) {
        DispenserBlock.registerBehavior(bucket, DISPENSE_FLUID);
    }

	public static void register() {}

	private static TagKey<Fluid> commonTag(String path) {
		return TagKey.create(CBCRegistryUtils.getFluidRegistryKey(), CBCUtils.location("c", path));
	}

    // Copied from AllFluids$SolidRenderedPlaceableFluidType
    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return this.fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return this.fogDistance.get();
        }

    }

}
