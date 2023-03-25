package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.MouseScrolledCallback;
import io.github.fabricators_of_create.porting_lib.event.client.ParticleManagerRegistrationCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import rbasamoyai.createbigcannons.CBCClientCommon;

public class CBCClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(KeyBindingHelper::registerKeyBinding);

		ParticleManagerRegistrationCallback.EVENT.register(CBCClientFabric::onParticleRegistry);
		FogEvents.SET_COLOR.register(CBCClientFabric::setFogColor);
		FogEvents.SET_DENSITY.register(CBCClientFabric::getFogDensity);
		ClientTickEvents.END_CLIENT_TICK.register(CBCClientFabric::onClientTick);
		MouseScrolledCallback.EVENT.register(CBCClientFabric::onScrolledMouse);
	}

	public static void onParticleRegistry() {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static float getFogDensity(Camera info, float currentDensity) {
		return CBCClientCommon.getFogDensity(info, currentDensity);
	}

	public static void setFogColor(FogEvents.ColorData data, float partialTicks) {
		CBCClientCommon.setFogColor(data.getCamera(), (r, g, b) -> {
			data.setRed(r);
			data.setGreen(g);
			data.setBlue(b);
		});
	}

	public static void onClientTick(Minecraft mc) {
		CBCClientCommon.onClientGameTick(mc);
	}

	public static boolean onScrolledMouse(double delta) {
		return CBCClientCommon.onScrollMouse(Minecraft.getInstance(), delta);
	}

}
