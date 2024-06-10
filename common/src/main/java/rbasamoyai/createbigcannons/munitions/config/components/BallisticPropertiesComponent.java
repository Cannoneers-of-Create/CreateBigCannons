package rbasamoyai.createbigcannons.munitions.config.components;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record BallisticPropertiesComponent(double gravity, double drag, boolean isQuadraticDrag, float durabilityMass) {

	public static final BallisticPropertiesComponent DEFAULT = new BallisticPropertiesComponent(0, 0, false, 0);

	public static BallisticPropertiesComponent fromJson(String id, JsonObject obj) {
		double gravity = Math.min(0, GsonHelper.getAsDouble(obj, "gravity", -0.05d));
		double drag = Math.max(0, GsonHelper.getAsDouble(obj, "drag", 0.0d));
		boolean isQuadraticDrag = GsonHelper.getAsBoolean(obj, "quadratic_drag", false);
		float durabilityMass = Math.max(0, getOrWarn(obj, "durability_mass", id, 1f, JsonElement::getAsFloat));
		return new BallisticPropertiesComponent(gravity, drag, isQuadraticDrag, durabilityMass);
	}

	public static BallisticPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BallisticPropertiesComponent(buf.readDouble(), buf.readDouble(), buf.readBoolean(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.gravity)
			.writeDouble(this.drag)
			.writeBoolean(this.isQuadraticDrag)
			.writeFloat(this.durabilityMass);
	}

}
