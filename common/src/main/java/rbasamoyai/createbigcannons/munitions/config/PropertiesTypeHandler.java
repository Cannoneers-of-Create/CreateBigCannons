package rbasamoyai.createbigcannons.munitions.config;

import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public abstract class PropertiesTypeHandler<TYPE, PROPERTIES> {

	private final Map<TYPE, PROPERTIES> propertiesByType = new Reference2ObjectOpenHashMap<>();

	public final void clearForReload() {
		this.propertiesByType.clear();
	}
	public final void loadFromJson(TYPE type, ResourceLocation loc, JsonObject obj) throws JsonParseException {
		this.propertiesByType.put(type, this.parseJson(loc, obj));
	}

	protected abstract PROPERTIES parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException;

	public static <V> V getOrWarn(JsonObject obj, String key, String id, V defValue, Function<JsonElement, V> func) {
		if (!obj.has(key)) {
			CreateBigCannons.LOGGER.warn("{} is missing {} value, will be set to {}", id, key, defValue);
			return defValue;
		}
		return func.apply(obj.getAsJsonPrimitive(key));
	}

	public final void loadFromNetwork(TYPE type, FriendlyByteBuf buf) {
		this.propertiesByType.put(type, this.readPropertiesFromNetwork(type, buf));
	}

	public final void writeToNetwork(TYPE type, FriendlyByteBuf buf) {
		this.writePropertiesToNetwork(this.propertiesByType.get(type), buf);
	}

	protected abstract PROPERTIES readPropertiesFromNetwork(TYPE type, FriendlyByteBuf buf);
	protected abstract void writePropertiesToNetwork(PROPERTIES properties, FriendlyByteBuf buf);

	@Nonnull
	public final PROPERTIES getPropertiesOf(TYPE type) {
		return this.propertiesByType.getOrDefault(type, this.getNoPropertiesValue());
	}

	protected abstract PROPERTIES getNoPropertiesValue();

}
