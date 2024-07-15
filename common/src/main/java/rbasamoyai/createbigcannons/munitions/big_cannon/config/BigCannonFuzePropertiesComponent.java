package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record BigCannonFuzePropertiesComponent(boolean baseFuze) {

	public static final BigCannonFuzePropertiesComponent DEFAULT = new BigCannonFuzePropertiesComponent(false);

	public static BigCannonFuzePropertiesComponent fromJson(String id, JsonObject obj) {
		boolean baseFuze = GsonHelper.getAsBoolean(obj, "base_fuze", false);
		return new BigCannonFuzePropertiesComponent(baseFuze);
	}

	public static BigCannonFuzePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new BigCannonFuzePropertiesComponent(buf.readBoolean());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeBoolean(this.baseFuze);
	}

}
