package rbasamoyai.createbigcannons.crafting;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonCastingRecipe {

	private final CannonCastShape requiredShape;
	private final FluidIngredient ingredient;
	private final Block result;
	private final ResourceLocation id;
	
	public CannonCastingRecipe(CannonCastShape requiredShape, FluidIngredient ingredient, Block result, ResourceLocation id) {
		this.requiredShape = requiredShape;
		this.ingredient = ingredient;
		this.result = result;
		this.id = id;
	}
	
	public CannonCastShape shape() { return this.requiredShape; }
	public FluidIngredient ingredient() { return this.ingredient; }
	public Block result() { return this.result; }
	public ResourceLocation id() { return this.id; }
	
	public JsonObject serializeRecipe() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", CreateBigCannons.resource("cannon_casting").toString());
		obj.addProperty("cast_shape", this.requiredShape.name().toString());
		obj.add("fluid", this.ingredient().serialize());
		obj.addProperty("result", this.result.getRegistryName().toString());
		return obj;
	}
	
}
