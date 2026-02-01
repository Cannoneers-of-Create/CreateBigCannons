package rbasamoyai.createbigcannons.base;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CBCRegistries {

	public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
	public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES = key("block_recipe_types");
	public static final ResourceKey<Registry<CannonCastShape>> CANNON_CAST_SHAPES = key("cannon_cast_shapes");

	private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }

	@SuppressWarnings("unchecked")
	public static Registry<BlockRecipeSerializer<?>> blockRecipeSerializers() {
		return (Registry<BlockRecipeSerializer<?>>) BuiltInRegistries.REGISTRY.get(BLOCK_RECIPE_SERIALIZERS.location());
	}

	@SuppressWarnings("unchecked")
	public static Registry<BlockRecipeType<?>> blockRecipeTypes() {
		return (Registry<BlockRecipeType<?>>) BuiltInRegistries.REGISTRY.get(BLOCK_RECIPE_TYPES.location());
	}

	@SuppressWarnings("unchecked")
	public static Registry<CannonCastShape> cannonCastShapes() {
		return (Registry<CannonCastShape>) BuiltInRegistries.REGISTRY.get(CANNON_CAST_SHAPES.location());
	}

}
