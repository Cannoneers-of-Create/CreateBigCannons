package rbasamoyai.createbigcannons.multiloader;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.CreateBigCannonsNeoForge;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.mixin.ContextAwareReloadListenerAccessor;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlockEntity;

public class IndexPlatform {

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
		return FluidFX.getFluidParticle(new FluidStack(Holder.direct(stack.fluid()), stack.amount(), stack.components().asPatch()));
	}

	public static NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context,
		BlockEntityRenderer<? super AbstractCannonCastBlockEntity>>> getCastRenderer() {
		return () -> CannonCastBlockEntityRenderer::new;
	}

	public static void registerDeferredParticleType(String name, ParticleType<?> type) {
		CreateBigCannonsNeoForge.PARTICLE_REGISTER.register(name, () -> type);
	}

	public static void registerDeferredParticles() {

	}

	@OnlyIn(Dist.CLIENT)
	public static KeyMapping createSafeKeyMapping(String description, InputConstants.Type type, int keycode) {
		return new KeyMapping(description, type, keycode, "key." + CreateBigCannons.MOD_ID + ".category");
	}

	@OnlyIn(Dist.CLIENT)
	public static <T extends ItemPropertyFunction> void registerClampedItemProperty(Item item, ResourceLocation loc, T func) {
		ItemProperties.register(item, loc, func);
	}

	@OnlyIn(Dist.CLIENT)
	public static <T extends ItemPropertyFunction> void registerGenericClampedItemProperty(ResourceLocation loc, T func) {
		ItemProperties.registerGeneric(loc, func);
	}

	public static Supplier<RecipeSerializer<?>> registerRecipeSerializer(ResourceLocation id, NonNullSupplier<RecipeSerializer<?>> sup) {
		return CreateBigCannonsNeoForge.RECIPE_SERIALIZER_REGISTER.register(id.getPath(), sup);
	}

	public static void registerRecipeType(ResourceLocation id, Supplier<RecipeType<?>> type) {
		CreateBigCannonsNeoForge.RECIPE_TYPE_REGISTER.register(id.getPath(), type);
	}

	public static float getFluidConversionFactor() {
		return 1;
	}

	public static FluidIngredient fluidIngredientFrom(Fluid fluid, int amount) {
		return FluidIngredient.of(fluid);
	}

	public static FluidIngredient fluidIngredientFrom(TagKey<Fluid> fluid, int amount) {
		return FluidIngredient.tag(fluid);
	}

	public static void addFluidShellComponents(Fluid fluid, DataComponentPatch fluidData, long amount, List<Component> tooltip) {
		int capacity = AbstractFluidShellBlockEntity.getFluidShellCapacity();
		LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
		if (fluid != Fluids.EMPTY && amount > 0) {
			CreateLang.translate("gui.goggles.fluid_container").addTo(tooltip);
			CreateLang.text(" ")
				.add(CreateLang.fluidName(new FluidStack(Holder.direct(fluid), 1, fluidData)).style(ChatFormatting.GRAY))
				.addTo(tooltip);

			CreateLang.text(" ")
				.add(CreateLang.builder()
				.add(CreateLang.number(amount).add(mb).style(ChatFormatting.GOLD))
				.text(ChatFormatting.GRAY, " / ")
				.add(CreateLang.number(capacity).add(mb).style(ChatFormatting.DARK_GRAY)))
				.addTo(tooltip);
		} else {
			CreateLang.translate("gui.goggles.fluid_container.capacity")
				.add(CreateLang.number(capacity).add(mb).style(ChatFormatting.GOLD))
				.style(ChatFormatting.GRAY)
				.addTo(tooltip);
		}
	}

	public static boolean onExplosionStart(Level level, Explosion explosion) {
		return EventHooks.onExplosionStart(level, explosion);
	}

	@OnlyIn(Dist.CLIENT)
	public static void updateSprite(TerrainParticle particle, BlockState state, BlockPos pos) {
		particle.updateSprite(state, pos);
	}

    public static RegistryOps<JsonElement> makeRegistryOps(RegistryAccess access, SimpleJsonResourceReloadListener listener) {
        return ((ContextAwareReloadListenerAccessor) listener).callMakeConditionalOps();
    }

}
