package rbasamoyai.createbigcannons.munitions.autocannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record AutocannonProjectilePropertiesComponent(double addedRecoil, boolean canSquib) {

	public static final AutocannonProjectilePropertiesComponent DEFAULT = new AutocannonProjectilePropertiesComponent(0, false);

	public static AutocannonProjectilePropertiesComponent fromJson(String id, JsonObject obj) {
		double addedRecoil = Math.max(0, GsonHelper.getAsDouble(obj, "added_recoil", 1));
		boolean canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
		return new AutocannonProjectilePropertiesComponent(addedRecoil, canSquib);
	}

	public static AutocannonProjectilePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new AutocannonProjectilePropertiesComponent(buf.readDouble(), buf.readBoolean());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.addedRecoil)
			.writeBoolean(this.canSquib);
	}

}
