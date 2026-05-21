package rbasamoyai.createbigcannons.munitions.config.components;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record ExplosionPropertiesComponent(float blockDamagePower, float entityDamagePower) {

	public static final ExplosionPropertiesComponent DEFAULT = new ExplosionPropertiesComponent(0, 0);

	public static ExplosionPropertiesComponent fromJson(String id, JsonObject obj) {
        if (GsonHelper.isNumberValue(obj, "explosive_power")) {
            float explosionPower = Math.max(0, getOrWarn(obj, "explosive_power", id, 2f, JsonElement::getAsFloat));
            return new ExplosionPropertiesComponent(explosionPower, explosionPower);
        } else {
            float blockPower = Math.max(0, getOrWarn(obj, "block_damaging_explosive_power", id, 2f, JsonElement::getAsFloat));
            float entityPower = Math.max(0, getOrWarn(obj, "entity_damaging_explosive_power", id, 2f, JsonElement::getAsFloat));
            return new ExplosionPropertiesComponent(blockPower, entityPower);
        }
	}

	public static ExplosionPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new ExplosionPropertiesComponent(buf.readFloat(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.blockDamagePower).writeFloat(this.entityDamagePower);
	}

}
