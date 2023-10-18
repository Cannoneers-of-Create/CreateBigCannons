package rbasamoyai.createbigcannons.fabric;

import com.mojang.blaze3d.vertex.PoseStack;

import io.github.fabricators_of_create.porting_lib.event.client.CameraSetupCallback;
import io.github.fabricators_of_create.porting_lib.event.client.FieldOfViewEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.MouseInputEvents;
import io.github.fabricators_of_create.porting_lib.event.client.ParticleManagerRegistrationCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.fabric.mixin.client.KeyMappingAccessor;
import rbasamoyai.createbigcannons.fabric.network.CBCNetworkFabric;

public class CBCClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(KeyBindingHelper::registerKeyBinding);

		CBCNetworkFabric.INSTANCE.initClientListener();

		ParticleManagerRegistrationCallback.EVENT.register(CBCClientFabric::onParticleRegistry);
		FogEvents.SET_COLOR.register(CBCClientFabric::setFogColor);
		FogEvents.SET_DENSITY.register(CBCClientFabric::getFogDensity);
		ClientTickEvents.END_CLIENT_TICK.register(CBCClientFabric::onClientTick);
		MouseInputEvents.AFTER_SCROLL.register(CBCClientFabric::onScrolledMouse);
		FieldOfViewEvents.MODIFY.register(CBCClientFabric::getFov);
		ScreenEvents.BEFORE_INIT.register(CBCClientFabric::onOpenScreen);
		LivingEntityRenderEvents.PRE.register(CBCClientFabric::onBeforeRender);
		CameraSetupCallback.EVENT.register(CBCClientFabric::onSetupCamera);
		ClientLoginConnectionEvents.DISCONNECT.register(CBCClientFabric::onPlayerLogOut);
	}

	public static void onParticleRegistry() {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static float getFogDensity(Camera info, float currentDensity) {
		float density = CBCClientCommon.getFogDensity(info, currentDensity);
		return density == -1 ? currentDensity : density;
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

	public static boolean onScrolledMouse(double deltaX, double deltaY) {
		return CBCClientCommon.onScrollMouse(Minecraft.getInstance(), deltaY);
	}

	public static float getFov(Player player, float oldFov) {
		return CBCClientCommon.onFovModify(Minecraft.getInstance(), oldFov);
	}

	public static void keyInput(int key, int scancode, boolean flag) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.screen != null) return;
		CBCClientCommon.KEYS.stream().filter(k -> k.matches(key, scancode)).forEach(k -> setInput(k, flag));
	}

	public static void mouseInput(int button, boolean flag) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.screen != null) return;
		CBCClientCommon.KEYS.stream().filter(k -> k.matchesMouse(button)).forEach(k -> setInput(k, flag));
	}

	public static void onOpenScreen(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		CBCClientCommon.KEYS.forEach(k -> setInput(k, false));
	}

	private static void setInput(KeyMapping k, boolean flag) {
		k.setDown(flag);
		if (flag) {
			KeyMappingAccessor acc = (KeyMappingAccessor) k;
			acc.setClickCount(acc.getClickCount() + 1);
		}
	}

	public static boolean onBeforeRender(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialRenderTick,
									  PoseStack matrixStack, MultiBufferSource buffers, int light) {
		if (entity.getType() == EntityType.PLAYER) {
			CBCClientCommon.onPlayerRenderPre(matrixStack, entity, partialRenderTick);
		}
		return false;
	}

	public static boolean onSetupCamera(CameraSetupCallback.CameraInfo info) {
		return CBCClientCommon.onCameraSetup(info.camera, info.partialTicks, () -> info.yaw, () -> info.pitch, () -> info.roll,
			y -> info.yaw = y, p -> info.pitch = p, r -> info.roll = r);
	}

	public static void onPlayerLogOut(ClientHandshakePacketListenerImpl impl, Minecraft client) {
		CBCClientCommon.onPlayerLogOut(client.player);
	}

}
