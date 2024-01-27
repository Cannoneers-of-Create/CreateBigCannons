package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer;

import static rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesSerializer.getOrWarn;

public class BigCartridgeProperties extends BigCannonPropellantProperties {

	private final int maxPowerLevels;

	public BigCartridgeProperties(float strength, float addedStress, float addedRecoil, float addedSpread, int maxPowerLevels) {
		super(strength, addedStress, addedRecoil, addedSpread);
		this.maxPowerLevels = maxPowerLevels;
	}

	public BigCartridgeProperties(String id, JsonObject obj) {
		super(id, obj);
		this.maxPowerLevels = Mth.clamp(getOrWarn(obj, "maximum_power_levels", id, 4, JsonElement::getAsInt), 1, 20);
	}

	public BigCartridgeProperties(FriendlyByteBuf buf) {
		super(buf);
		this.maxPowerLevels = buf.readVarInt();
	}

	@Override
	public void toNetwork(FriendlyByteBuf buf) {
		super.toNetwork(buf);
		buf.writeVarInt(this.maxPowerLevels);
	}

	public int maxPowerLevels() { return this.maxPowerLevels; }

	public static class Serializer implements MunitionPropertiesSerializer<BigCartridgeProperties> {
		@Override
		public BigCartridgeProperties fromJson(ResourceLocation loc, JsonObject obj) {
			return new BigCartridgeProperties(loc.toString(), obj);
		}

		@Override
		public BigCartridgeProperties fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
			return new BigCartridgeProperties(buf);
		}
	}

}
