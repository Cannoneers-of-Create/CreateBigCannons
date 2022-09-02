package rbasamoyai.createbigcannons.crafting.casting;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonCastingRecipe {

	private final CannonCastShape requiredShape;
	private final FluidIngredient ingredient;
	private final Block result;
	private final int castingTime;
	private final ResourceLocation id;
	
	public CannonCastingRecipe(CannonCastShape requiredShape, FluidIngredient ingredient, Block result, int castingTime, ResourceLocation id) {
		this.requiredShape = requiredShape;
		this.ingredient = ingredient;
		this.result = result;
		this.castingTime = castingTime;
		this.id = id;
	}
	
	public CannonCastShape shape() { return this.requiredShape; }
	public FluidIngredient ingredient() { return this.ingredient; }
	public Block result() { return this.result; }
	public int castingTime() { return this.castingTime; }
	public ResourceLocation id() { return this.id; }
	
	public JsonObject serializeRecipe() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", CreateBigCannons.resource("cannon_casting").toString());
		obj.addProperty("cast_shape", this.requiredShape.name().toString());
		obj.add("fluid", this.ingredient().serialize());
		obj.addProperty("casting_time", this.castingTime);
		obj.addProperty("result", this.result.getRegistryName().toString());
		return obj;
	}
	
	public void toBuffer(FriendlyByteBuf buf) {
		buf.writeResourceLocation(this.requiredShape.name())
		.writeVarInt(this.castingTime)
		.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, this.result);
		this.ingredient.write(buf);
	}
	
	public static CannonCastingRecipe fromJson(ResourceLocation loc, JsonObject obj) {
		CannonCastShape shape = CannonCastShape.byId(new ResourceLocation(obj.get("cast_shape").getAsString()));
		FluidIngredient ingredient = FluidIngredient.deserialize(obj.get("fluid"));
		int castingTime = obj.get("casting_time").getAsInt();
		Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("result").getAsString()));
		return new CannonCastingRecipe(shape, ingredient, result, castingTime, loc);
	}
	
	public static CannonCastingRecipe fromBuf(ResourceLocation id, FriendlyByteBuf buf) {
		CannonCastShape shape = CannonCastShape.byId(buf.readResourceLocation());
		int castingTime = buf.readVarInt();
		Block result = buf.readRegistryIdUnsafe(ForgeRegistries.BLOCKS);
		FluidIngredient ingredient = FluidIngredient.read(buf);
		return new CannonCastingRecipe(shape, ingredient, result, castingTime, id);
	}
	
}
