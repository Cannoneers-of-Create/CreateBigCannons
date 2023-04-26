package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rbasamoyai.createbigcannons.CreateBigCannons;

import javax.annotation.Nullable;
import java.util.function.Function;

public record MunitionProperties(double entityDamage, double explosivePower, double durabilityMass, boolean rendersInvulnerable,
								 boolean ignoresEntityArmor, @Nullable ShrapnelProperties shrapnel) {

	public static MunitionProperties fromJson(JsonObject obj, String id) {
		double entityDmg = Math.max(0, getOrWarn(obj, "entity_damage", id, 1d, JsonElement::getAsDouble));
		double durabilityMass = Math.max(0, getOrWarn(obj, "durability_mass", id, 1d, JsonElement::getAsDouble));

		double explosivePower = Math.max(0, obj.has("explosive_power") ? obj.get("explosive_power").getAsDouble() : 0);
		boolean rendersInvulnerable = !obj.has("renders_invulnerable") || obj.get("renders_invulnerable").getAsBoolean();
		boolean ignoresEntityArmor = obj.has("ignores_entity_armor") && obj.get("ignores_entity_armor").getAsBoolean();

		ShrapnelProperties shrapnel = obj.has("shrapnel_properties")
				? ShrapnelProperties.fromJson(obj.getAsJsonObject("shrapnel_properties"), id) : null;

		return new MunitionProperties(entityDmg, explosivePower, durabilityMass, rendersInvulnerable, ignoresEntityArmor, shrapnel);
	}

	private static <T> T getOrWarn(JsonObject obj, String key, String id, T defValue, Function<JsonElement, T> func) {
		if (!obj.has(key)) {
			CreateBigCannons.LOGGER.warn("{} is missing {} value, will be set to 1", id, key);
			return defValue;
		}
		return func.apply(obj.getAsJsonPrimitive(key));
	}

	public JsonElement serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("entity_damage", this.entityDamage);
		obj.addProperty("durability_mass", this.durabilityMass);
		obj.addProperty("renders_invulnerable", this.rendersInvulnerable);
		obj.addProperty("ignores_entity_armor", this.ignoresEntityArmor);

		if (this.explosivePower > 0) obj.addProperty("explosive_power", this.explosivePower);
		if (this.shrapnel != null) {
			obj.add("shrapnel_properties", this.shrapnel.serialize());
		}

		return obj;
	}

	@Override
	public ShrapnelProperties shrapnel() {
		return this.shrapnel == null ? ShrapnelProperties.DEFAULT : this.shrapnel;
	}
}
