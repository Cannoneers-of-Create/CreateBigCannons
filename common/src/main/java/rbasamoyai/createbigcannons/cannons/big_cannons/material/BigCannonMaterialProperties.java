package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BigCannonMaterialProperties(double minimumVelocityPerBarrel, float weight, int maxSafePropellantStress,
										  FailureMode failureMode, boolean connectsInSurvival,
										  boolean isWeldable, int weldDamage, int weldStressPenalty) {

	public boolean mayGetStuck(float chargesUsed, int barrelTravelled) {
		if (this.minimumVelocityPerBarrel < 0) return true;
		if (barrelTravelled < 1) return false;
		return this.minimumVelocityPerBarrel > chargesUsed / barrelTravelled;
	}

	public static BigCannonMaterialProperties fromJson(JsonObject obj) {
		double minimumVelocityPerBarrel = Math.max(-1, GsonHelper.getAsDouble(obj, "minimum_velocity_per_barrel"));
		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));
		int maxSafeBaseCharges = Math.max(0, GsonHelper.getAsInt(obj, "max_safe_charges", 2));
		FailureMode failureMode = FailureMode.byId(GsonHelper.getAsString(obj, "failure_mode"));
		boolean connectsInSurvival = GsonHelper.getAsBoolean(obj, "connects_in_survival", false);
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		return new BigCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, failureMode,
			connectsInSurvival, isWeldable, weldDamage, weldStressPenalty);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("minimum_velocity_per_barrel", this.minimumVelocityPerBarrel);
		obj.addProperty("weight", this.weight);
		obj.addProperty("max_safe_charges", this.maxSafePropellantStress);
		obj.addProperty("failure_mode", this.failureMode.toString());
		obj.addProperty("connects_in_survival", this.connectsInSurvival);
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeDouble(this.minimumVelocityPerBarrel)
			.writeFloat(this.weight);
		buf.writeVarInt(this.maxSafePropellantStress)
			.writeVarInt(this.failureMode.ordinal())
			.writeBoolean(this.connectsInSurvival)
			.writeBoolean(this.isWeldable);
		buf.writeVarInt(this.weldDamage)
			.writeVarInt(this.weldStressPenalty);
	}

	public static BigCannonMaterialProperties fromBuf(FriendlyByteBuf buf) {
		double minimumVelocityPerBarrel = buf.readDouble();
		float weight = buf.readFloat();
		int maxSafeBaseCharges = buf.readVarInt();
		FailureMode mode = FailureMode.values()[buf.readVarInt()];
		boolean connectsInSurvival = buf.readBoolean();
		boolean isWeldable = buf.readBoolean();
		int weldDamage = buf.readVarInt();
		int weldStressPenalty = buf.readVarInt();
		return new BigCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, mode, connectsInSurvival,
			isWeldable, weldDamage, weldStressPenalty);
	}

	public enum FailureMode implements StringRepresentable {
		RUPTURE,
		FRAGMENT;

		private static final Map<String, FailureMode> BY_ID = Arrays.stream(values())
			.collect(Collectors.toMap(StringRepresentable::getSerializedName, Function.identity()));

		private final String name;

		FailureMode() {
			this.name = this.name().toLowerCase();
		}

		@Override public String getSerializedName() { return this.name; }
		public static FailureMode byId(String id) { return BY_ID.getOrDefault(id, RUPTURE); }
	}

}
