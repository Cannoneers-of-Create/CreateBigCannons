package rbasamoyai.createbigcannons.base;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryBuilder;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCRegistries {

	public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS_KEY =
			REGISTRATE.makeRegistry("block_recipe_serializers", CBCRegistries::makeRegBlockRecipeSerializer);
	
	public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES_KEY =
			REGISTRATE.makeRegistry("block_recipe_types", CBCRegistries::makeRegBlockRecipeType);
	
	public static final ResourceKey<Registry<CannonCastShape>> CANNON_CAST_SHAPES_KEY =
			REGISTRATE.makeRegistry("cannon_cast_shapes", CBCRegistries::makeRegCannonCastShape);

	public static <T> Registry<T> getRegistry(ResourceKey<Registry<T>> key) {
		return (Registry<T>) Registry.REGISTRY.get(key.location());
	}

	private static RegistryBuilder<BlockRecipeSerializer<?>> makeRegBlockRecipeSerializer() {
		return new RegistryBuilder<BlockRecipeSerializer<?>>().allowModification().hasTags();
	}
	
	private static RegistryBuilder<BlockRecipeType<?>> makeRegBlockRecipeType() {
		return new RegistryBuilder<BlockRecipeType<?>>().allowModification().hasTags();
	}
	
	private static RegistryBuilder<CannonCastShape> makeRegCannonCastShape() {
		return new RegistryBuilder<CannonCastShape>().allowModification().hasTags();
	}
	
	public static void init() {}
	
}
