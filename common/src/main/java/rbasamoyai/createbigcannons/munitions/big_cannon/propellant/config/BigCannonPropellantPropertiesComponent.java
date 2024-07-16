package rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record BigCannonPropellantPropertiesComponent(float strength, float addedStress, float addedRecoil, float addedSpread) {

	public static final BigCannonPropellantPropertiesComponent DEFAULT = new BigCannonPropellantPropertiesComponent(0, 0, 0, 0);

	public static BigCannonPropellantPropertiesComponent fromJson(String id, JsonObject obj) {
		float strength = Math.max(0, GsonHelper.getAsFloat(obj, "strength", 2));
		float addedStress = Math.max(0, GsonHelper.getAsFloat(obj, "added_stress", 1));
		float addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 2));
		float addedSpread = Math.max(0, GsonHelper.getAsFloat(obj, "added_spread", 1));
		return new BigCannonPropellantPropertiesComponent(strength, addedStress, addedRecoil, addedSpread);
	}

	public static BigCannonPropellantPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BigCannonPropellantPropertiesComponent(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.strength)
			.writeFloat(this.addedStress)
			.writeFloat(this.addedRecoil)
			.writeFloat(this.addedSpread);
	}

}
