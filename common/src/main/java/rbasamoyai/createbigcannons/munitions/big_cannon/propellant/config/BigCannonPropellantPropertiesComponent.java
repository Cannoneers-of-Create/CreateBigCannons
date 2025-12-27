package rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record BigCannonPropellantPropertiesComponent(float strength, float addedStress, float addedRecoil, float addedSpread,
													 float explosionPower, float dampAmmoStrengthDebuff, boolean dampAmmoDoesntIgniteAsStarter) {

	public static final BigCannonPropellantPropertiesComponent DEFAULT = new BigCannonPropellantPropertiesComponent(0, 0, 0, 0, 0, 0, false);

	public static BigCannonPropellantPropertiesComponent fromJson(String id, JsonObject obj) {
		float strength = Math.max(0, GsonHelper.getAsFloat(obj, "strength", 2));
		float addedStress = Math.max(0, GsonHelper.getAsFloat(obj, "added_stress", 1));
		float addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 2));
		float addedSpread = Math.max(0, GsonHelper.getAsFloat(obj, "added_spread", 1));
		float explosionPower = Math.max(0, GsonHelper.getAsFloat(obj, "explosion_power", 4));
		float dampAmmoPowerDebuff = Math.max(0, GsonHelper.getAsFloat(obj, "damp_ammo_strength_debuff", 0.5f));
		boolean dampAmmoDoesntIgniteAsStarter = GsonHelper.getAsBoolean(obj, "damp_ammo_doesnt_ignite_as_starter", true);
		return new BigCannonPropellantPropertiesComponent(strength, addedStress, addedRecoil, addedSpread, explosionPower,
			dampAmmoPowerDebuff, dampAmmoDoesntIgniteAsStarter);
	}

	public static BigCannonPropellantPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BigCannonPropellantPropertiesComponent(buf.readFloat(), buf.readFloat(), buf.readFloat(),
			buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readBoolean());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.strength)
			.writeFloat(this.addedStress)
			.writeFloat(this.addedRecoil)
			.writeFloat(this.addedSpread)
			.writeFloat(this.explosionPower)
			.writeFloat(this.dampAmmoStrengthDebuff)
			.writeBoolean(this.dampAmmoDoesntIgniteAsStarter);
	}

}
