package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BigCannonMaterialProperties(int squibRatioNum, int squibRatioDem, float weight, int maxSafePropellantStress,
										  FailureMode failureMode, boolean isWeldable, int weldDamage, int weldStressPenalty) {

	/**
	 * The squib ratio describes the maximum ratio of <b>cannon barrel</b> to
	 * <b>meters per gametick</b> that cannons of this material can safely
	 * operate at. If the actual squib ratio exceeds this value, the cannon
	 * squib chance is increased.
	 *
	 * @return A double representing the squib ratio of the material
	 */
	public double squibRatio() {
		return this.squibRatioDem == 0 ? 0 : (double) this.squibRatioNum / (double) this.squibRatioDem;
	}

	public static BigCannonMaterialProperties fromJson(JsonObject obj) {
		int squibRatioNum = Math.max(0, GsonHelper.getAsInt(obj, "squib_ratio_barrels"));
		int squibRatioDem = Math.max(1, GsonHelper.getAsInt(obj, "squib_ratio_propellant"));
		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));
		int maxSafeBaseCharges = Math.max(0, GsonHelper.getAsInt(obj, "max_safe_charges", 2));
		FailureMode failureMode = FailureMode.byId(GsonHelper.getAsString(obj, "failure_mode"));
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		return new BigCannonMaterialProperties(squibRatioNum, squibRatioDem, weight, maxSafeBaseCharges, failureMode,
			isWeldable, weldDamage, weldStressPenalty);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("squib_ratio_barrels", this.squibRatioNum);
		obj.addProperty("squib_ratio_propellant", this.squibRatioDem);
		obj.addProperty("weight", this.weight);
		obj.addProperty("max_safe_charges", this.maxSafePropellantStress);
		obj.addProperty("failure_mode", this.failureMode.toString());
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.squibRatioNum)
			.writeVarInt(this.squibRatioDem)
			.writeFloat(this.weight);
		buf.writeVarInt(this.maxSafePropellantStress)
			.writeVarInt(this.failureMode.ordinal())
			.writeBoolean(this.isWeldable);
		buf.writeVarInt(this.weldDamage)
			.writeVarInt(this.weldStressPenalty);
	}

	public static BigCannonMaterialProperties fromBuf(FriendlyByteBuf buf) {
		int squibRatioNum = buf.readVarInt();
		int squibRatioDem = buf.readVarInt();
		float weight = buf.readFloat();
		int maxSafeBaseCharges = buf.readVarInt();
		FailureMode mode = FailureMode.values()[buf.readVarInt()];
		boolean isWeldable = buf.readBoolean();
		int weldDamage = buf.readVarInt();
		int weldStressPenalty = buf.readVarInt();
		return new BigCannonMaterialProperties(squibRatioNum, squibRatioDem, weight, maxSafeBaseCharges, mode, isWeldable, weldDamage, weldStressPenalty);
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
