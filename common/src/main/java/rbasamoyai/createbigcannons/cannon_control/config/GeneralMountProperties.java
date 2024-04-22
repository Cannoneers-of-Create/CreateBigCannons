package rbasamoyai.createbigcannons.cannon_control.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record GeneralMountProperties(float maximumElevation, float maximumDepression) {

	public static GeneralMountProperties fromJson(JsonObject obj) {
		float maximumElevation = 0;
		float maximumDepression = 0;
		if (obj.has("pitch_range") && obj.get("pitch_range").isJsonObject()) {
			JsonObject range = obj.getAsJsonObject("pitch_range");
			maximumElevation = GsonHelper.getAsFloat(range, "maximum_elevation");
			maximumDepression = GsonHelper.getAsFloat(range, "maximum_depression");
		}
		return new GeneralMountProperties(maximumElevation, maximumDepression);
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.maximumElevation)
			.writeFloat(this.maximumDepression);
	}

	public static GeneralMountProperties fromNetwork(FriendlyByteBuf buf) {
		float maximumElevation = buf.readFloat();
		float maximumDepression = buf.readFloat();
		return new GeneralMountProperties(maximumElevation, maximumDepression);
	}

}
