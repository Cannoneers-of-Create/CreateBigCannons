package rbasamoyai.createbigcannons.index;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.AutocannonAmmoContainerFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.BigCartridgeFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.BigCartridgeFillingRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.CartridgeAssemblyDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.CartridgeAssemblyRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.FuzeRemovalRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.MunitionFuzingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.MunitionFuzingRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.TracerApplicationDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.TracerApplicationRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.TracerRemovalRecipe;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public enum CBCRecipeTypes implements IRecipeTypeInfo {

	MELTING(MeltingRecipe::new),
	MUNITION_FUZING(noSerializer(MunitionFuzingRecipe::new)),
	CARTRIDGE_ASSEMBLY(noSerializer(CartridgeAssemblyRecipe::new)),
	BIG_CARTRIDGE_FILLING(noSerializer(BigCartridgeFillingRecipe::new)),
	BIG_CARTRIDGE_FILLING_DEPLOYER(noSerializer(BigCartridgeFillingDeployerRecipe::new)),
	MUNITION_FUZING_DEPLOYER(noSerializer(MunitionFuzingDeployerRecipe::new)),
	CARTRIDGE_ASSEMBLY_DEPLOYER(noSerializer(CartridgeAssemblyDeployerRecipe::new)),
	TRACER_APPLICATION(noSerializer(TracerApplicationRecipe::new)),
	TRACER_APPLICATION_DEPLOYER(noSerializer(TracerApplicationDeployerRecipe::new)),
	AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER(noSerializer(AutocannonAmmoContainerFillingDeployerRecipe::new)),
	FUZE_REMOVAL(noSerializer(FuzeRemovalRecipe::new)),
	TRACER_REMOVAL(noSerializer(TracerRemovalRecipe::new));

	private final ResourceLocation id;
	private final Supplier<RecipeSerializer<?>> serializerObject;
	@Nullable
	private final RecipeType<?> typeObject;
	private final NonNullSupplier<RecipeType<?>> type;

	CBCRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier, NonNullSupplier<RecipeType<?>> typeSupplier, boolean registerType) {
		String name = CreateLang.asId(name());
		id = CreateBigCannons.resource(name);
		serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
		if (registerType) {
			typeObject = typeSupplier.get();
			IndexPlatform.registerRecipeType(this.id, typeSupplier);
			type = typeSupplier;
		} else {
			typeObject = null;
			type = typeSupplier;
		}
	}

	CBCRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier) {
		String name = CreateLang.asId(name());
		id = CreateBigCannons.resource(name);
		serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
		typeObject = simpleType(id);
		type = () -> typeObject;
		IndexPlatform.registerRecipeType(this.id, this.type);
	}

	CBCRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
		this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
	}

	public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
		String stringId = id.toString();
		return new RecipeType<>() {
			@Override
			public String toString() {
				return stringId;
			}
		};
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) this.serializerObject.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
		return (RecipeType<R>) this.type.get();
	}

	public static void register() {
	}

	private static <T extends Recipe<?>> NonNullSupplier<RecipeSerializer<?>> noSerializer(Supplier<T> prov) {
		return () -> new SimpleRecipeSerializer<>(prov);
	}

	private static class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
        private final MapCodec<T> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

		public SimpleRecipeSerializer(Supplier<T> constructor) {
            this.codec = MapCodec.unit(constructor);
            this.streamCodec = StreamCodec.of((buf, r) -> {}, buf -> constructor.get());
		}

        @Override public MapCodec<T> codec() { return this.codec; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() { return this.streamCodec; }
    }

}
