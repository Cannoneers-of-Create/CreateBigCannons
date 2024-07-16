package rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.config.BlockPropertiesTypeHandler;

public class BigCartridgePropertiesHandler extends BlockPropertiesTypeHandler<BigCartridgeProperties> {

	private static final BigCartridgeProperties DEFAULT = new BigCartridgeProperties(BigCannonPropellantPropertiesComponent.DEFAULT, 0);

	@Override
	protected BigCartridgeProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BigCannonPropellantPropertiesComponent propellantProperties = BigCannonPropellantPropertiesComponent.fromJson(id, obj);
		int maxPowerLevels = Mth.clamp(getOrWarn(obj, "maximum_power_levels", id, 4, JsonElement::getAsInt), 1, 20);
		return new BigCartridgeProperties(propellantProperties, maxPowerLevels);
	}

	@Override
	protected BigCartridgeProperties readPropertiesFromNetwork(Block block, FriendlyByteBuf buf) {
		BigCannonPropellantPropertiesComponent propellantProperties = BigCannonPropellantPropertiesComponent.fromNetwork(buf);
		int maxPowerLevels = buf.readVarInt();
		return new BigCartridgeProperties(propellantProperties, maxPowerLevels);
	}

	@Override
	protected void writePropertiesToNetwork(BigCartridgeProperties properties, FriendlyByteBuf buf) {
		properties.propellantProperties().toNetwork(buf);
		buf.writeVarInt(properties.maxPowerLevels());
	}

	@Override protected BigCartridgeProperties getNoPropertiesValue() { return DEFAULT; }

}
