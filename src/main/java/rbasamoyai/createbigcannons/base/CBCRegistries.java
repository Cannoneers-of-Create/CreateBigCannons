package rbasamoyai.createbigcannons.base;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CBCRegistries {

	public static final Supplier<IForgeRegistry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS =
			CreateBigCannons.registrate().makeRegistry("block_recipe_serializers", BlockRecipeSerializer.class, CBCRegistries::makeRegBlockRecipeSerializer);
	
	public static final Supplier<IForgeRegistry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES =
			CreateBigCannons.registrate().makeRegistry("block_recipe_types", BlockRecipeType.class, CBCRegistries::makeRegBlockRecipeType);
	
	public static final Supplier<IForgeRegistry<CannonCastShape>> CANNON_CAST_SHAPES =
			CreateBigCannons.registrate().makeRegistry("cannon_cast_shapes", CannonCastShape.class, CBCRegistries::makeRegCannonCastShape);
	
	private static RegistryBuilder<BlockRecipeSerializer<?>> makeRegBlockRecipeSerializer() {
		return new RegistryBuilder<BlockRecipeSerializer<?>>().allowModification();
	}
	
	private static RegistryBuilder<BlockRecipeType<?>> makeRegBlockRecipeType() {
		return new RegistryBuilder<BlockRecipeType<?>>().allowModification();
	}
	
	private static RegistryBuilder<CannonCastShape> makeRegCannonCastShape() {
		return new RegistryBuilder<CannonCastShape>().allowModification();
	}
	
	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES = key("block_recipe_types");
		public static final ResourceKey<Registry<CannonCastShape>> CANNON_CAST_SHAPES = key("cannon_cast_shapes");
		
		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}
	
	public static void init() {}
	
}
