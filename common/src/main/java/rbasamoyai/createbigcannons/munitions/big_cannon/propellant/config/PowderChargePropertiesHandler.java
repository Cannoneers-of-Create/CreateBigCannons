package rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.config.BlockPropertiesTypeHandler;

public class PowderChargePropertiesHandler extends BlockPropertiesTypeHandler<PowderChargeProperties> {

	private static final PowderChargeProperties DEFAULT = new PowderChargeProperties(BigCannonPropellantPropertiesComponent.DEFAULT);

	@Override
	protected PowderChargeProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		BigCannonPropellantPropertiesComponent propellantProperties = BigCannonPropellantPropertiesComponent.fromJson(location.toString(), obj);
		return new PowderChargeProperties(propellantProperties);
	}

	@Override
	protected PowderChargeProperties readPropertiesFromNetwork(Block block, FriendlyByteBuf buf) {
		BigCannonPropellantPropertiesComponent propellantProperties = BigCannonPropellantPropertiesComponent.fromNetwork(buf);
		return new PowderChargeProperties(propellantProperties);
	}

	@Override
	protected void writePropertiesToNetwork(PowderChargeProperties properties, FriendlyByteBuf buf) {
		properties.propellantProperties().toNetwork(buf);
	}

	@Override protected PowderChargeProperties getNoPropertiesValue() { return DEFAULT; }

}
