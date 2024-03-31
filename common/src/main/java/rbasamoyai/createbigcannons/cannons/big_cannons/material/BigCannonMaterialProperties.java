package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import rbasamoyai.createbigcannons.CreateBigCannons;

public record BigCannonMaterialProperties(double minimumVelocityPerBarrel, float weight, int maxSafePropellantStress,
										  FailureMode failureMode, boolean connectsInSurvival, boolean isWeldable,
										  int weldDamage, int weldStressPenalty, float minimumSpread, float spreadReductionPerBarrel) {

	public boolean mayGetStuck(float chargesUsed, int barrelTravelled) {
		if (this.minimumVelocityPerBarrel < 0) return true;
		if (barrelTravelled < 1) return false;
		return this.minimumVelocityPerBarrel > chargesUsed / barrelTravelled;
	}

	public static BigCannonMaterialProperties fromJson(ResourceLocation id, JsonObject obj) {
		double minimumVelocityPerBarrel = GsonHelper.getAsDouble(obj, "minimum_velocity_per_barrel", 1);
		// Legacy config handling
		if (GsonHelper.isNumberValue(obj, "squib_ratio_barrels") && GsonHelper.isNumberValue(obj, "squib_ratio_propellant")) {
			float barrels = (float) GsonHelper.getAsInt(obj, "squib_ratio_barrels");
			float propellant = (float) GsonHelper.getAsInt(obj, "squib_ratio_propellant");
			float ratio = propellant <= 0 ? -1 : barrels / propellant;
			if (ratio < 0) ratio = -1;
			CreateBigCannons.LOGGER.warn("Legacy values \"squib_ratio_barrels\" and \"squib_ratio_propellant\" found in config for {}, please change to \"max_safe_propellant_stress\". Recommended value: {}", id, String.format(".%2f", ratio));
			if (!GsonHelper.isNumberValue(obj, "minimum_velocity_per_barrel")) {
				minimumVelocityPerBarrel = ratio;
			}
		}
		minimumVelocityPerBarrel = Math.max(-1, minimumVelocityPerBarrel);

		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));

		int maxSafeBaseCharges = GsonHelper.getAsInt(obj, "max_safe_propellant_stress", 2);
		// Legacy config handling
		if (GsonHelper.isNumberValue(obj, "max_safe_charges")) {
			CreateBigCannons.LOGGER.warn("Legacy value \"max_safe_charges\" found in config for {}, please change to \"max_safe_propellant_stress\"", id);
			if (!GsonHelper.isNumberValue(obj, "max_safe_propellant_stress"))
				maxSafeBaseCharges = GsonHelper.getAsInt(obj, "max_safe_charges");
		}
		maxSafeBaseCharges = Math.max(0, maxSafeBaseCharges);

		FailureMode failureMode = FailureMode.byId(GsonHelper.getAsString(obj, "failure_mode"));
		boolean connectsInSurvival = GsonHelper.getAsBoolean(obj, "connects_in_survival", false);
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		float minimumSpread = Math.max(GsonHelper.getAsFloat(obj, "minimum_spread", 0.05f), 0);
		float spreadReductionPerBarrel = Math.max(GsonHelper.getAsFloat(obj, "spread_reduction_per_barrel", 1), 0);
		return new BigCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, failureMode,
			connectsInSurvival, isWeldable, weldDamage, weldStressPenalty, minimumSpread, spreadReductionPerBarrel);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("minimum_velocity_per_barrel", this.minimumVelocityPerBarrel);
		obj.addProperty("weight", this.weight);
		obj.addProperty("max_safe_propellant_stress", this.maxSafePropellantStress);
		obj.addProperty("failure_mode", this.failureMode.toString());
		obj.addProperty("connects_in_survival", this.connectsInSurvival);
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		obj.addProperty("minimum_spread", this.minimumSpread);
		obj.addProperty("spread_reduction_per_barrel", this.spreadReductionPerBarrel);
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
			.writeVarInt(this.weldStressPenalty)
			.writeFloat(this.minimumSpread)
			.writeFloat(this.spreadReductionPerBarrel);
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
		float minimumSpread = buf.readFloat();
		float spreadReductionPerBarrel = buf.readFloat();
		return new BigCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, mode, connectsInSurvival,
			isWeldable, weldDamage, weldStressPenalty, minimumSpread, spreadReductionPerBarrel);
	}

	public enum FailureMode implements StringRepresentable {
		RUPTURE,
		FRAGMENT;

		private static final Map<String, FailureMode> BY_ID = Arrays.stream(values())
			.collect(Collectors.toMap(StringRepresentable::getSerializedName, Function.identity()));

		private final String name;

		FailureMode() {
			this.name = this.name().toLowerCase(Locale.ROOT);
		}

		@Override public String getSerializedName() { return this.name; }
		public static FailureMode byId(String id) { return BY_ID.getOrDefault(id, RUPTURE); }
	}

}
