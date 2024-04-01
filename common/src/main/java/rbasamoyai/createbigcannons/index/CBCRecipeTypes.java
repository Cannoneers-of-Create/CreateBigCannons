package rbasamoyai.createbigcannons.index;

import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
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
	BIG_CARTRIDGE_FILLING_DEPLOYER(noSerializer(r -> new BigCartridgeFillingDeployerRecipe())),
	MUNITION_FUZING_DEPLOYER(noSerializer(r -> new MunitionFuzingDeployerRecipe())),
	CARTRIDGE_ASSEMBLY_DEPLOYER(noSerializer(r -> new CartridgeAssemblyDeployerRecipe())),
	TRACER_APPLICATION(noSerializer(TracerApplicationRecipe::new)),
	TRACER_APPLICATION_DEPLOYER(noSerializer(r -> new TracerApplicationDeployerRecipe())),
	AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER(noSerializer(r -> new AutocannonAmmoContainerFillingDeployerRecipe())),
	FUZE_REMOVAL(noSerializer(FuzeRemovalRecipe::new)),
	TRACER_REMOVAL(noSerializer(TracerRemovalRecipe::new));

	private final ResourceLocation id;
	private final Supplier<RecipeSerializer<?>> serializerObject;
	@Nullable
	private final RecipeType<?> typeObject;
	private final NonNullSupplier<RecipeType<?>> type;

	CBCRecipeTypes(NonNullSupplier<RecipeSerializer<?>> serializerSupplier, NonNullSupplier<RecipeType<?>> typeSupplier, boolean registerType) {
		String name = Lang.asId(name());
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
		String name = Lang.asId(name());
		id = CreateBigCannons.resource(name);
		serializerObject = IndexPlatform.registerRecipeSerializer(this.id, serializerSupplier);
		typeObject = simpleType(id);
		type = () -> typeObject;
		IndexPlatform.registerRecipeType(this.id, this.type);
	}

	CBCRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
		this(() -> new ProcessingRecipeSerializer<>(processingFactory));
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

	@Override public ResourceLocation getId() { return this.id; }

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() { return (T) this.serializerObject.get(); }

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() { return (T) this.type.get(); }

	public static void register() {
	}

	private static <T extends Recipe<?>> NonNullSupplier<RecipeSerializer<?>> noSerializer(Function<ResourceLocation, T> prov) {
		return () -> new SimpleRecipeSerializer<>(prov);
	}

	private static class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
		private final Function<ResourceLocation, T> constructor;

		public SimpleRecipeSerializer(Function<ResourceLocation, T> constructor) {
			this.constructor = constructor;
		}

		@Override
		public T fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
			return this.constructor.apply(recipeId);
		}

		@Override
		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return this.constructor.apply(recipeId);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, T recipe) {
		}
	}

}
