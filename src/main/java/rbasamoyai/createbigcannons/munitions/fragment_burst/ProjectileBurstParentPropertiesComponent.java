package rbasamoyai.createbigcannons.munitions.fragment_burst;

import static rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler.getOrWarn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;

public record ProjectileBurstParentPropertiesComponent(double burstSpread, int burstProjectileCount) {

	public static final ProjectileBurstParentPropertiesComponent DEFAULT = new ProjectileBurstParentPropertiesComponent(0, 0);

	public static ProjectileBurstParentPropertiesComponent fromJson(String id, String prefix, JsonObject obj) {
		double spread = Math.max(0, getOrWarn(obj, prefix + "spread", id, 1d, JsonElement::getAsDouble));
		int count = Math.max(0, getOrWarn(obj, prefix + "count", id, 1, JsonElement::getAsInt));
		return new ProjectileBurstParentPropertiesComponent(spread, count);
	}

	public static ProjectileBurstParentPropertiesComponent fromJson(String id, JsonObject obj) { return fromJson(id, "burst_", obj); }

	public static ProjectileBurstParentPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new ProjectileBurstParentPropertiesComponent(buf.readFloat(), buf.readVarInt());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat((float) this.burstSpread);
		buf.writeVarInt(this.burstProjectileCount);
	}

}
