package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record DropMortarProjectilePropertiesComponent(float mortarPower, float mortarRecoil, float mortarSpread) {

	public static final DropMortarProjectilePropertiesComponent DEFAULT = new DropMortarProjectilePropertiesComponent(0, 0, 0);

	public static DropMortarProjectilePropertiesComponent fromJson(String id, JsonObject obj) {
		float mortarPower = Math.max(0, GsonHelper.getAsInt(obj, "mortar_power", 3));
		float mortarRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "mortar_recoil", 1));
		float mortarSpread = Math.max(0, GsonHelper.getAsFloat(obj, "mortar_spread", 0.1f));
		return new DropMortarProjectilePropertiesComponent(mortarPower, mortarRecoil, mortarSpread);
	}

	public static DropMortarProjectilePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new DropMortarProjectilePropertiesComponent(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.mortarPower)
			.writeFloat(this.mortarRecoil)
			.writeFloat(this.mortarSpread);
	}

}
