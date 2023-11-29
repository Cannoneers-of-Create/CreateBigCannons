package rbasamoyai.createbigcannons.base;

import com.mojang.serialization.Lifecycle;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CBCRegistries {

	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES = key("block_recipe_types");
		public static final ResourceKey<Registry<CannonCastShape>> CANNON_CAST_SHAPES = key("cannon_cast_shapes");

		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}

	public static MappedRegistry<BlockRecipeSerializer<?>> BLOCK_RECIPE_SERIALIZERS;
	public static MappedRegistry<BlockRecipeType<?>> BLOCK_RECIPE_TYPES;
	public static MappedRegistry<CannonCastShape> CANNON_CAST_SHAPES;

	@SuppressWarnings("rawtypes")
	private static <T> MappedRegistry<T> makeRegistrySimple(ResourceKey<? extends Registry<T>> key) {
		MappedRegistry<T> registry = new MappedRegistry<>(key, Lifecycle.stable(), null);
		WritableRegistry root = (WritableRegistry) Registry.REGISTRY;
		root.register(key, registry, Lifecycle.stable());
		return registry;
	}

	private static boolean initialized = false;

	public static void actualInit() {
		if (initialized) return;
		initialized = true;
		BLOCK_RECIPE_SERIALIZERS = makeRegistrySimple(Keys.BLOCK_RECIPE_SERIALIZERS);
		BLOCK_RECIPE_TYPES = makeRegistrySimple(Keys.BLOCK_RECIPE_TYPES);
		CANNON_CAST_SHAPES = makeRegistrySimple(Keys.CANNON_CAST_SHAPES);

		CannonCastShape.register();
		BlockRecipeSerializer.register();
		BlockRecipeType.register();
	}

	public static void forceInit() {}

	@ExpectPlatform public static void onClinit() { throw new AssertionError(); }

	static {
		onClinit();
	}

}
