package rbasamoyai.createbigcannons.munitions.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.function.Function;

public interface MunitionPropertiesSerializer<T extends MunitionProperties> {

	T fromJson(ResourceLocation loc, JsonObject obj);
	T fromNetwork(ResourceLocation loc, FriendlyByteBuf buf);

	default void toNetwork(FriendlyByteBuf buf, T properties) {
		properties.toNetwork(buf);
	}

	static <T> T getOrWarn(JsonObject obj, String key, String id, T defValue, Function<JsonElement, T> func) {
		if (!obj.has(key)) {
			CreateBigCannons.LOGGER.warn("{} is missing {} value, will be set to {}", id, key, defValue);
			return defValue;
		}
		return func.apply(obj.getAsJsonPrimitive(key));
	}

}
