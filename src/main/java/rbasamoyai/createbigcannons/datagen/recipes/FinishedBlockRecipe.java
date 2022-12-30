package rbasamoyai.createbigcannons.datagen.recipes;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;

public interface FinishedBlockRecipe {
	void serializeRecipeData(JsonObject obj);
	ResourceLocation getId();
	BlockRecipeSerializer<?> getSerializer();
	
	default JsonObject serializeRecipe() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", CBCRegistries.BLOCK_RECIPE_SERIALIZERS.get().getKey(this.getSerializer()).toString());
		this.serializeRecipeData(obj);
		return obj;
	}
}
