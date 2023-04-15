package rbasamoyai.createbigcannons.multiloader;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

import java.util.function.Supplier;

public class IndexPlatform {

	@ExpectPlatform public static boolean isFakePlayer(Player player) { throw new AssertionError(); }

	@ExpectPlatform
	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractCannonCastBlockEntity makeCast(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractAutocannonBreechBlockEntity makeAutocannonBreech(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractFluidShellBlockEntity makeFluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform public static AbstractMountedAutocannonContraption makeAutocannon() { throw new AssertionError(); }

	@ExpectPlatform
	public static AbstractPitchOrientedContraptionEntity makePitchContraption(EntityType<?> type, Level level) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractCannonCarriageEntity makeCannonCarriage(EntityType<?> type, Level level) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ParticleOptions createFluidDripParticle(EndFluidStack stack) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context,
					BlockEntityRenderer<? super AbstractCannonCastBlockEntity>>> getCastRenderer() {
		throw new AssertionError();
	}

	@ExpectPlatform public static int getModGroupId() { throw new AssertionError(); }

	@SuppressWarnings("unchecked") public static <T extends DataGenerator> T castGen(DataGenerator gen) { return (T) gen; }

	@ExpectPlatform
	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> createFluidBuilder(AbstractRegistrate<?> owner,
			P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
			NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
		throw new AssertionError();
	}

	@ExpectPlatform public static void registerDeferredParticleType(String name, ParticleType<?> type) { throw new AssertionError(); }

	@ExpectPlatform public static void registerDeferredParticles() { throw new AssertionError(); }

	@Environment(EnvType.CLIENT)
	@ExpectPlatform
	public static KeyMapping createSafeKeyMapping(String description, InputConstants.Type type, int keycode) {
		throw new AssertionError();
	}

	@Environment(EnvType.CLIENT)
	@ExpectPlatform
	public static <T extends ItemPropertyFunction> void registerClampedItemProperty(Item item, ResourceLocation loc, T func) {
		throw new AssertionError();
	}

	@ExpectPlatform public static Object getUnchecked(RegistryEntry<?> ent) { throw new AssertionError(); }

	@ExpectPlatform
	public static Supplier<RecipeSerializer<?>> registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
		throw new AssertionError();
	}

	@ExpectPlatform public static float getFluidConversionFactor() { throw new AssertionError(); }
	public static int convertFluid(int forgeMb) { return Mth.ceil((float) forgeMb * getFluidConversionFactor()); }

	@ExpectPlatform public static void addSidedDataGenerators(DataGenerator gen) { throw new AssertionError(); }

	@ExpectPlatform public static FluidIngredient fluidIngredientFrom(Fluid fluid, int amount) { throw new AssertionError(); }
	@ExpectPlatform public static FluidIngredient fluidIngredientFrom(TagKey<Fluid> fluid, int amount) { throw new AssertionError(); }

}
