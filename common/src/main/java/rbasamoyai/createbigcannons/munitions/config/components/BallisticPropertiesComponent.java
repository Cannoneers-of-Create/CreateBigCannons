package rbasamoyai.createbigcannons.munitions.config.components;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;

public record BallisticPropertiesComponent(double gravity, double drag, boolean isQuadraticDrag, float durabilityMass,
										   float penetration, float toughness, float deflection) {

	public static final BallisticPropertiesComponent DEFAULT = new BallisticPropertiesComponent(0, 0, false, 0, 0, 0, 0);

	public static BallisticPropertiesComponent fromJson(String id, JsonObject obj) {
		double gravity = Math.min(0, GsonHelper.getAsDouble(obj, "gravity", -0.05d));
		double drag = Math.max(0, GsonHelper.getAsDouble(obj, "drag", 0.0d));
		boolean isQuadraticDrag = GsonHelper.getAsBoolean(obj, "quadratic_drag", false);
		float durabilityMass = Math.max(0, getOrWarn(obj, "durability_mass", id, 1f, JsonElement::getAsFloat));
		float penetration = Math.max(0, getOrWarn(obj, "penetration", id, 1f, JsonElement::getAsFloat));
		float toughness = Math.max(0, getOrWarn(obj, "toughness", id, 1f, JsonElement::getAsFloat));
		float deflection = Mth.clamp(getOrWarn(obj, "deflection", id, 1f, JsonElement::getAsFloat), 0, 1);
		return new BallisticPropertiesComponent(gravity, drag, isQuadraticDrag, durabilityMass, penetration, toughness, deflection);
	}

	public static BallisticPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BallisticPropertiesComponent(buf.readDouble(), buf.readDouble(), buf.readBoolean(), buf.readFloat(),
			buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.gravity)
			.writeDouble(this.drag)
			.writeBoolean(this.isQuadraticDrag)
			.writeFloat(this.durabilityMass)
			.writeFloat(this.penetration)
			.writeFloat(this.toughness)
			.writeFloat(this.deflection);
	}

}
