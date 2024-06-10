package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record BigCannonProjectilePropertiesComponent(float addedChargePower, float minimumChargePower, boolean canSquib, float addedRecoil) {

	public static final BigCannonProjectilePropertiesComponent DEFAULT = new BigCannonProjectilePropertiesComponent(0, 0, false, 0);

	public static BigCannonProjectilePropertiesComponent fromJson(String id, JsonObject obj) {
		float addedChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "added_charge_power", 0));
		float minimumChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "minimum_charge_power", 1));
		boolean canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
		float addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 1));
		return new BigCannonProjectilePropertiesComponent(addedChargePower, minimumChargePower, canSquib, addedRecoil);
	}

	public static BigCannonProjectilePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BigCannonProjectilePropertiesComponent(buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.addedChargePower)
			.writeFloat(this.minimumChargePower)
			.writeBoolean(this.canSquib)
			.writeFloat(this.addedRecoil);
	}

}
