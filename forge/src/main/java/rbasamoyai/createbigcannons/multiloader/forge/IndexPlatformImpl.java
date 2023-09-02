package rbasamoyai.createbigcannons.multiloader.forge;

import java.util.function.Supplier;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.CreateBigCannonsForge;
import rbasamoyai.createbigcannons.forge.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.forge.crafting.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.forge.datagen.CBCCompactingRecipeProvider;
import rbasamoyai.createbigcannons.forge.datagen.CBCCuttingRecipeProvider;
import rbasamoyai.createbigcannons.forge.datagen.CBCLootTableProvider;
import rbasamoyai.createbigcannons.forge.datagen.CBCMillingRecipeProvider;
import rbasamoyai.createbigcannons.forge.datagen.CBCMixingRecipeProvider;
import rbasamoyai.createbigcannons.forge.datagen.CBCSequencedAssemblyRecipeProvider;
import rbasamoyai.createbigcannons.forge.datagen.MeltingRecipeProvider;
import rbasamoyai.createbigcannons.forge.index.fluid_utils.ForgeFluidBuilder;
import rbasamoyai.createbigcannons.forge.munitions.fluid_shell.FluidShellBlockEntity;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;
import rbasamoyai.createbigcannons.index.fluid_utils.FluidBuilder;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

public class IndexPlatformImpl {

	public static boolean isFakePlayer(Player player) {
		return player instanceof FakePlayer;
	}

	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CannonDrillBlockEntity(type, pos, state);
	}

	public static AbstractCannonCastBlockEntity makeCast(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CannonCastBlockEntity(type, pos, state);
	}

	public static AbstractAutocannonBreechBlockEntity makeAutocannonBreech(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new AutocannonBreechBlockEntity(type, pos, state);
	}

	public static AbstractFluidShellBlockEntity makeFluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new FluidShellBlockEntity(type, pos, state);
	}

	public static ParticleOptions createFluidDripParticle(EndFluidStack stack) {
		return FluidFX.getFluidParticle(new FluidStack(stack.fluid(), stack.amount(), stack.data()));
	}

	public static NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context,
		BlockEntityRenderer<? super AbstractCannonCastBlockEntity>>> getCastRenderer() {
		return () -> CannonCastBlockEntityRenderer::new;
	}

	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> createFluidBuilder(AbstractRegistrate<?> owner,
																					   P parent, String name, BuilderCallback callback, ResourceLocation stillTexture, ResourceLocation flowingTexture,
																					   NonNullFunction<CBCFlowingFluid.Properties, T> factory) {
		return new ForgeFluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture, factory);
	}

	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
		return builder;
	}

	public static void registerDeferredParticleType(String name, ParticleType<?> type) {
		CreateBigCannonsForge.PARTICLE_REGISTER.register(name, () -> type);
	}

	public static void registerDeferredParticles() {
		CreateBigCannonsForge.PARTICLE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@OnlyIn(Dist.CLIENT)
	public static KeyMapping createSafeKeyMapping(String description, InputConstants.Type type, int keycode) {
		return new KeyMapping(description, type, keycode, "key." + CreateBigCannons.MOD_ID + ".category");
	}

	@OnlyIn(Dist.CLIENT)
	public static <T extends ItemPropertyFunction> void registerClampedItemProperty(Item item, ResourceLocation loc, T func) {
		ItemProperties.register(item, loc, func);
	}

	public static Supplier<RecipeSerializer<?>> registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
		return CreateBigCannonsForge.RECIPE_SERIALIZER_REGISTER.register(id.getPath(), sup);
	}

	public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
		CreateBigCannonsForge.RECIPE_TYPE_REGISTER.register(id.getPath(), type);
	}

	public static float getFluidConversionFactor() {
		return 1;
	}

	public static void addSidedDataGenerators(DataGenerator gen) {
		gen.addProvider(true, new CBCCompactingRecipeProvider(gen));
		gen.addProvider(true, new MeltingRecipeProvider(gen));
		gen.addProvider(true, new CBCMixingRecipeProvider(gen));
		gen.addProvider(true, new CBCMillingRecipeProvider(gen));
		gen.addProvider(true, new CBCSequencedAssemblyRecipeProvider(gen));
		gen.addProvider(true, new CBCCuttingRecipeProvider(gen));
		gen.addProvider(true, new CBCLootTableProvider(CreateBigCannons.REGISTRATE, gen));
	}

	public static FluidIngredient fluidIngredientFrom(Fluid fluid, int amount) {
		return FluidIngredient.fromFluid(fluid, amount);
	}

	public static FluidIngredient fluidIngredientFrom(TagKey<Fluid> fluid, int amount) {
		return FluidIngredient.fromTag(fluid, amount);
	}

	public static float modifyRotationStateYaw(boolean flag, boolean vertRotation, float yaw) {
		return flag && !vertRotation ? yaw + 180 : yaw;
	}

}
