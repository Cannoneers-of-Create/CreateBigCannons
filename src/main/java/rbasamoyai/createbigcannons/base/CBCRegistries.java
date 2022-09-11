package rbasamoyai.createbigcannons.base;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;

public class CBCRegistries {

	public static final Supplier<IForgeRegistry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS =
			CreateBigCannons.registrate().makeRegistry("block_recipe_serializers", BlockRecipeSerializer.class, CBCRegistries::makeRegBlockRecipeSerializer);	
	
	private static RegistryBuilder<BlockRecipeSerializer<?>> makeRegBlockRecipeSerializer() {
		return new RegistryBuilder<BlockRecipeSerializer<?>>().allowModification();
	}
	
	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		
		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}
	
	public static void init() {}
	
}
