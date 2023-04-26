package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.function.Function;

public record ShrapnelProperties(double damage, double spread, int count) {

	public static ShrapnelProperties DEFAULT = new ShrapnelProperties(0, 0, 0);

	public static ShrapnelProperties fromJson(JsonObject obj, String id) {
		double entityDmg = Math.max(0, getOrWarn(obj, "entity_damage", id, 1d, JsonElement::getAsDouble));
		double spread = Math.max(0, getOrWarn(obj, "entity_damage", id, 1d, JsonElement::getAsDouble));
		int count = Math.max(0, getOrWarn(obj, "entity_damage", id, 1, JsonElement::getAsInt));

		return new ShrapnelProperties(entityDmg, spread, count);
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
		obj.addProperty("entity_damage", this.damage);
		obj.addProperty("spread", this.spread);
		obj.addProperty("count", this.count);

		return obj;
	}

}
