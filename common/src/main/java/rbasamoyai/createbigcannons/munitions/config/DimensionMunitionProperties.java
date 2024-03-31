package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record DimensionMunitionProperties(double gravityMultiplier, double dragMultiplier) {

	public static DimensionMunitionProperties fromJson(JsonObject obj, String id) {
		double gravityMultiplier = Math.max(0, GsonHelper.getAsDouble(obj, "gravity_multiplier", 1));
		double dragMultiplier = Math.max(0, GsonHelper.getAsDouble(obj, "drag_multiplier", 1));
		return new DimensionMunitionProperties(gravityMultiplier, dragMultiplier);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("gravity_multiplier", this.gravityMultiplier);
		obj.addProperty("drag_multiplier", this.dragMultiplier);
		return obj;
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.gravityMultiplier)
			.writeDouble(this.dragMultiplier);
	}

	public static DimensionMunitionProperties fromNetwork(FriendlyByteBuf buf) {
		double gravityMultiplier = buf.readDouble();
		double dragMultiplier = buf.readDouble();
		return new DimensionMunitionProperties(gravityMultiplier, dragMultiplier);
	}

}
