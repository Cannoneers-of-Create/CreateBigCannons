package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Camera;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class CBCClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(KeyBindingHelper::registerKeyBinding);
		FogEvents.SET_COLOR.register(CBCClientFabric::setFogColor);
		FogEvents.SET_DENSITY.register(CBCClientFabric::getFogDensity);
	}

	public static float getFogDensity(Camera info, float currentDensity) {
		return CBCClientCommon.getFogDensity(info, currentDensity);
	}

}
