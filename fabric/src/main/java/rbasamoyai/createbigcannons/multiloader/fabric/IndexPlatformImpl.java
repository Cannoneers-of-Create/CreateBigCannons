package rbasamoyai.createbigcannons.multiloader.fabric;

import java.util.function.Supplier;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import io.github.fabricators_of_create.porting_lib.fake_players.FakePlayer;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.fabric.CreateBigCannonsFabric;
import rbasamoyai.createbigcannons.fabric.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.fabric.crafting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.fabric.crafting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.fabric.crafting.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.fabric.index.fluid_utils.FabricFluidBuilder;
import rbasamoyai.createbigcannons.fabric.mixin.client.KeyMappingAccessor;
import rbasamoyai.createbigcannons.fabric.munitions.fluid_shell.FluidShellBlockEntity;
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
		return new FabricFluidBuilder<>(owner, parent, name, callback, stillTexture, flowingTexture, factory);
	}

	public static <T extends CBCFlowingFluid, P> FluidBuilder<T, P> doFluidBuilderTransforms(FluidBuilder<T, P> builder) {
		FabricFluidBuilder<T, P> builderc = (FabricFluidBuilder<T, P>) builder;
		builderc.handleClientStuff();
		return builderc;
	}

	public static void registerDeferredParticleType(String name, ParticleType<?> type) {
		CreateBigCannonsFabric.PARTICLE_REGISTER.register(name, () -> type);
	}

	public static void registerDeferredParticles() {
		CreateBigCannonsFabric.PARTICLE_REGISTER.register();
	}

	// Provided by TelepathicGrunt - thanks! --ritchie
	@Environment(EnvType.CLIENT)
	public static KeyMapping createSafeKeyMapping(String description, InputConstants.Type type, int keycode) {
		InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(keycode);
		KeyMapping oldMapping = KeyMappingAccessor.getMAP().get(key);
		KeyMapping keyMapping = new KeyMapping(description, type, keycode, "key." + CreateBigCannons.MOD_ID + ".category");
		KeyMappingAccessor.getMAP().put(key, oldMapping);
		KeyMappingAccessor.getALL().remove(description);
		return keyMapping;
	}

	@Environment(EnvType.CLIENT)
	public static <T extends ItemPropertyFunction> void registerClampedItemProperty(Item item, ResourceLocation loc, T func) {
		ItemProperties.register(item, loc, func::call);
	}

	public static Supplier<RecipeSerializer<?>> registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
		RecipeSerializer<?> ret = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, sup.get());
		return () -> ret;
	}

	public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
		Registry.register(BuiltInRegistries.RECIPE_TYPE, id, type.get());
	}

	public static float getFluidConversionFactor() {
		return (float) FluidConstants.BUCKET / 1000;
	}

	public static FluidIngredient fluidIngredientFrom(Fluid fluid, int amount) {
		return FluidIngredient.fromFluid(fluid, amount);
	}

	public static FluidIngredient fluidIngredientFrom(TagKey<Fluid> fluid, int amount) {
		return FluidIngredient.fromTag(fluid, amount);
	}

}
