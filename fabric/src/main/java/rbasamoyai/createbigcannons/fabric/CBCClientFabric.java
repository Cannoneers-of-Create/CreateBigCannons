package rbasamoyai.createbigcannons.fabric;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import io.github.fabricators_of_create.porting_lib.event.client.CameraSetupCallback;
import io.github.fabricators_of_create.porting_lib.event.client.ClientWorldEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FieldOfViewEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.MouseInputEvents;
import io.github.fabricators_of_create.porting_lib.event.client.ParticleManagerRegistrationCallback;
import io.github.fabricators_of_create.porting_lib.event.common.ModsLoadedCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.compat.trinkets.CBCTrinketsClient;
import rbasamoyai.createbigcannons.fabric.mixin.client.KeyMappingAccessor;
import rbasamoyai.createbigcannons.fabric.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

public class CBCClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(KeyBindingHelper::registerKeyBinding);
		CBCClientCommon.registerOverlays("hotbar", CBCClientFabric::wrapOverlay);
		onRegisterClientReloadListeners();

		CBCNetworkFabric.INSTANCE.initClientListener();

		ParticleManagerRegistrationCallback.EVENT.register(CBCClientFabric::onParticleRegistry);
		FogEvents.SET_COLOR.register(CBCClientFabric::setFogColor);
		FogEvents.SET_DENSITY.register(CBCClientFabric::getFogDensity);
		ClientTickEvents.END_CLIENT_TICK.register(CBCClientFabric::onClientTick);
		MouseInputEvents.BEFORE_SCROLL.register(CBCClientFabric::onScrolledMouse);
		FieldOfViewEvents.MODIFY.register(CBCClientFabric::getFov);
		ScreenEvents.BEFORE_INIT.register(CBCClientFabric::onOpenScreen);
		LivingEntityRenderEvents.PRE.register(CBCClientFabric::onBeforeRender);
		CameraSetupCallback.EVENT.register(CBCClientFabric::onSetupCamera);
		MouseInputEvents.BEFORE_BUTTON.register(CBCClientFabric::onClickMouse);
		ClientPlayConnectionEvents.DISCONNECT.register(CBCClientFabric::onPlayerLogOut);
		ClientWorldEvents.LOAD.register(CBCClientFabric::onLoadClientLevel);
		ClientLoginConnectionEvents.INIT.register(CBCClientFabric::onPlayerLogIn);
		ModsLoadedCallback.EVENT.register(CBCClientFabric::onModsLoaded);
		CoreShaderRegistrationCallback.EVENT.register(CBCClientFabric::onShaderReload);
	}

	private static void wrapOverlay(String id, CBCClientCommon.CBCGuiOverlay overlay) {
		HudRenderCallback.EVENT.register((stack, partialTicks) -> {
			Window window = Minecraft.getInstance().getWindow();
			overlay.renderOverlay(stack, partialTicks, window.getGuiScaledWidth(), window.getGuiScaledHeight());
		});
	}

	public static void onModsLoaded(EnvType envType) {
		if (envType != EnvType.CLIENT)
			return;
		CBCModsFabric.TRINKETS.executeIfInstalled(() -> () -> CBCTrinketsClient.initClient());
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

	public static boolean onClickMouse(int button, int mods, MouseInputEvents.Action action) {
		if (action != MouseInputEvents.Action.PRESS) return false;
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen != null) return false;
		KeyMapping mapping = null;
		if (mc.options.keyUse.matchesMouse(button)) mapping = mc.options.keyUse;
		if (mc.options.keyAttack.matchesMouse(button)) mapping = mc.options.keyAttack;
		return mapping != null && CBCClientCommon.onClickMouse(mapping);
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

	public static void onLoadClientLevel(Minecraft minecraft, ClientLevel level) {
		CBCClientCommon.onLoadClientLevel(level);
	}

	public static void onPlayerLogOut(ClientPacketListener impl, Minecraft client) {
		CBCClientCommon.onPlayerLogOut(client.player);
	}

	public static void onPlayerLogIn(ClientHandshakePacketListenerImpl impl, Minecraft client) {
		CBCClientCommon.onPlayerLogIn(client.player);
	}

	public static void onRegisterClientReloadListeners() {
		CBCClientCommon.registerClientReloadListeners(CBCClientFabric::wrapAndRegisterReloadListener);
	}

	public static void wrapAndRegisterReloadListener(PreparableReloadListener base, ResourceLocation location) {
		IdentifiableResourceReloadListener listener = new IdentifiableResourceReloadListener() {
			@Override public ResourceLocation getFabricId() { return location; }

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
				return base.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
			}
		};

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener);
	}

	public static void onShaderReload(CoreShaderRegistrationCallback.RegistrationContext context) throws IOException {
		for (CBCRenderTypes renderType : CBCRenderTypes.values())
			context.register(renderType.id(), renderType.renderType().format(), renderType::setShaderInstance);
	}

}
