package rbasamoyai.createbigcannons.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.base.CBCRegistries;

public interface BlockRecipeSerializer<T extends BlockRecipe> {

	T fromJson(ResourceLocation id, JsonObject obj);
	T fromNetwork(ResourceLocation id, FriendlyByteBuf buf);
	void toNetwork(FriendlyByteBuf buf, T recipe);
	
	BlockRecipeSerializer<CannonCastingRecipe> CANNON_CASTING = register("cannon_casting", new CannonCastingRecipe.Serializer());
	BlockRecipeSerializer<BuiltUpHeatingRecipe> BUILT_UP_HEATING = register("built_up_heating", new BuiltUpHeatingRecipe.Serializer());
	BlockRecipeSerializer<DrillBoringBlockRecipe> DRILL_BORING = register("drill_boring", new DrillBoringBlockRecipe.Serializer());
	
	private static <T extends BlockRecipe> BlockRecipeSerializer<T> register(String id, BlockRecipeSerializer<T> ser) {
		return Registry.register(CBCRegistries.BLOCK_RECIPE_SERIALIZERS, id, ser);
	}
	
	static void register() {}
	
}
