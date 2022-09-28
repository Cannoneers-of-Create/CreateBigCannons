package rbasamoyai.createbigcannons.base;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

public class CBCRegistries {

	public static final Supplier<IForgeRegistry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS =
			CreateBigCannons.registrate().makeRegistry("block_recipe_serializers", BlockRecipeSerializer.class, CBCRegistries::makeRegBlockRecipeSerializer);
	
	public static final Supplier<IForgeRegistry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES =
			CreateBigCannons.registrate().makeRegistry("block_recipe_types", BlockRecipeType.class, CBCRegistries::makeRegBlockRecipeType);
	
	private static RegistryBuilder<BlockRecipeSerializer<?>> makeRegBlockRecipeSerializer() {
		return new RegistryBuilder<BlockRecipeSerializer<?>>().allowModification();
	}
	
	private static RegistryBuilder<BlockRecipeType<?>> makeRegBlockRecipeType() {
		return new RegistryBuilder<BlockRecipeType<?>>().allowModification();
	}
	
	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES = key("block_recipe_types");
		
		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}
	
	public static void init() {}
	
}
