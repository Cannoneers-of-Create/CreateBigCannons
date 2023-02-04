package rbasamoyai.createbigcannons;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.crafting.item_munitions.CartridgeAssemblyRecipe;
import rbasamoyai.createbigcannons.crafting.item_munitions.MunitionFuzingRecipe;

public enum CBCRecipeTypes implements IRecipeTypeInfo {

	MELTING(MeltingRecipe::new),
	MUNITION_FUZING(() -> new SimpleRecipeSerializer<>(MunitionFuzingRecipe::new), () -> RecipeType.CRAFTING, false),
	CARTRIDGE_ASSEMBLY(() -> new SimpleRecipeSerializer<>(CartridgeAssemblyRecipe::new), () -> RecipeType.CRAFTING, false);

	private final ResourceLocation id;
	private final RegistryObject<RecipeSerializer<?>> serializer;
	@Nullable private final RegistryObject<RecipeType<?>> typeObj;
	private final Supplier<RecipeType<?>> type;

	CBCRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
		String name = Lang.asId(name());
		this.id = CreateBigCannons.resource(name);
		this.serializer = CBCRecipeTypes.Registries.SERIALIZERS.register(name, serializerSupplier);
		if (registerType) {
			this.typeObj = CBCRecipeTypes.Registries.RECIPE_TYPES.register(name, typeSupplier);
			this.type = this.typeObj;
		} else {
			this.typeObj = null;
			this.type = typeSupplier;
		}
	}
	
	CBCRecipeTypes(Supplier<RecipeSerializer<?>> serializer) {
		String name = Lang.asId(this.name());
		this.id = CreateBigCannons.resource(name);
		this.serializer = Registries.SERIALIZERS.register(name, serializer);
		this.typeObj = Registries.RECIPE_TYPES.register(name, () -> RecipeType.simple(this.id));
		this.type = this.typeObj;
	}
	
	CBCRecipeTypes(ProcessingRecipeFactory<?> factory) {
		this(() -> new ProcessingRecipeSerializer<>(factory));
	}
	
	@Override public ResourceLocation getId() { return this.id; }
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) this.serializer.get();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) this.type.get();
	}
	
	public static void register(IEventBus modEventBus) {
		Registries.SERIALIZERS.register(modEventBus);
		Registries.RECIPE_TYPES.register(modEventBus);
	}
	
	private static class Registries {
		private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreateBigCannons.MOD_ID);
		private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CreateBigCannons.MOD_ID);
	}

}
