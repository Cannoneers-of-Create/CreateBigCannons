package rbasamoyai.createbigcannons.munitions.config.components;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;

public record ExplosionPropertiesComponent(float explosivePower) {

	public static final ExplosionPropertiesComponent DEFAULT = new ExplosionPropertiesComponent(0);

	public static ExplosionPropertiesComponent fromJson(String id, JsonObject obj) {
		float explosionPower = Math.max(0, getOrWarn(obj, "explosive_power", id, 2f, JsonElement::getAsFloat));
		return new ExplosionPropertiesComponent(explosionPower);
	}

	public static ExplosionPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new ExplosionPropertiesComponent(buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.explosivePower);
	}

}
