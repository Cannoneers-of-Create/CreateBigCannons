package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.crafting.item_munitions.CartridgeAssemblyRecipe;
import rbasamoyai.createbigcannons.crafting.item_munitions.MunitionFuzingRecipe;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum CBCRecipeTypes implements IRecipeTypeInfo {

	MELTING(MeltingRecipe::new),
	MUNITION_FUZING(() -> new SimpleRecipeSerializer<>(MunitionFuzingRecipe::new), () -> RecipeType.CRAFTING, false),
	CARTRIDGE_ASSEMBLY(() -> new SimpleRecipeSerializer<>(CartridgeAssemblyRecipe::new), () -> RecipeType.CRAFTING, false);

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

	CBCRecipeTypes(ProcessingRecipeFactory<?> processingFactory) {
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
	
	@Override public ResourceLocation getId() {
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject.get();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}
	
	public static void register() {}
}
