package rbasamoyai.createbigcannons.forge;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.compat.curios.CBCCuriosRenderers;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();

		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterKeyMappings);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);
		modEventBus.addListener(CBCClientForge::onLoadComplete);
		modEventBus.addListener(CBCClientForge::onRegisterClientReloadListeners);
		modEventBus.addListener(CBCClientForge::onRegisterGuiOverlays);
		modEventBus.addListener(CBCClientForge::onRegisterShaders);

		forgeEventBus.addListener(CBCClientForge::getFogColor);
		forgeEventBus.addListener(CBCClientForge::getFogDensity);
		forgeEventBus.addListener(CBCClientForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientForge::onFovModify);
		forgeEventBus.addListener(CBCClientForge::onPlayerRenderPre);
		forgeEventBus.addListener(CBCClientForge::onSetupCamera);
		forgeEventBus.addListener(CBCClientForge::onPlayerLogOut);
		forgeEventBus.addListener(CBCClientForge::onClickMouse);
		forgeEventBus.addListener(CBCClientForge::onLoadClientLevel);
		forgeEventBus.addListener(CBCClientForge::onPlayerLogIn);
		forgeEventBus.addListener(CBCClientForge::onPlayerChangeDimension);

		CBCModsForge.CURIOS.executeIfInstalled(() -> () -> CBCCuriosRenderers.register(modEventBus, forgeEventBus));
	}

	private static void wrapOverlay(String id, CBCClientCommon.CBCGuiOverlay overlay, VanillaGuiOverlay renderOver,
									RegisterGuiOverlaysEvent event) {
		event.registerAbove(renderOver.id(), id, (gui, stack, partialTicks, width, height) -> {
			overlay.renderOverlay(stack, partialTicks, width, height);
		});
	}

	public static void onRegisterParticleFactories(RegisterParticleProvidersEvent event) {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCClientCommon.onClientSetup();
	}

	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		CBCClientCommon.registerKeyMappings(event::register);
	}

	public static void getFogColor(ViewportEvent.ComputeFogColor event) {
		CBCClientCommon.setFogColor(event.getCamera(), (r, g, b) -> {
			event.setRed(r);
			event.setGreen(g);
			event.setBlue(b);
		});
	}

	public static void getFogDensity(ViewportEvent.RenderFog event) {
		if (!event.isCancelable()) return;
		float density = CBCClientCommon.getFogDensity(event.getCamera(), event.getFarPlaneDistance());
		if (density != -1) {
			event.setFarPlaneDistance(density);
			event.setNearPlaneDistance(density);
			event.setCanceled(true);
		}
	}

	public static void onClientGameTick(TickEvent.ClientTickEvent evt) {
		CBCClientCommon.onClientGameTick(Minecraft.getInstance());
	}

	public static void onClickMouse(InputEvent.InteractionKeyMappingTriggered evt) {
		if (CBCClientCommon.onClickMouse(evt.getKeyMapping()) && evt.isCancelable()) evt.setCanceled(true);
	}

	public static void onScrollMouse(InputEvent.MouseScrollingEvent evt) {
		if (CBCClientCommon.onScrollMouse(Minecraft.getInstance(), evt.getScrollDelta()) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onFovModify(ComputeFovModifierEvent evt) {
		evt.setNewFovModifier(CBCClientCommon.onFovModify(Minecraft.getInstance(), evt.getNewFovModifier()));
	}

	public static void onPlayerRenderPre(RenderPlayerEvent.Pre evt) {
		CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), evt.getEntity(), evt.getPartialTick());
	}

	public static void onSetupCamera(ViewportEvent.ComputeCameraAngles evt) {
		if (CBCClientCommon.onCameraSetup(evt.getCamera(), evt.getPartialTick(), evt::getYaw, evt::getPitch, evt::getRoll,
			evt::setYaw, evt::setPitch, evt::setRoll) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onLoadClientLevel(LevelEvent.Load evt) {
		LevelAccessor level = evt.getLevel();
		if (!level.isClientSide())
			return;
		CBCClientCommon.onLoadClientLevel(level);
	}

	public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut evt) {
		CBCClientCommon.onPlayerLogOut(evt.getPlayer());
	}

	public static void onPlayerLogIn(ClientPlayerNetworkEvent.LoggingIn evt) {
		CBCClientCommon.onPlayerLogIn(evt.getPlayer());
	}

	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent evt) {
		CBCClientCommon.onChangeDimension(evt.getEntity());
	}

	public static void onLoadComplete(FMLLoadCompleteEvent evt) {
		ModContainer container = ModList.get()
			.getModContainerById(CreateBigCannons.MOD_ID)
			.orElseThrow(() -> new IllegalStateException("CBC mod container missing on LoadComplete"));
		container.registerExtensionPoint(ConfigScreenFactory.class,
			() -> new ConfigScreenFactory((mc, screen) -> CBCConfigs.createConfigScreen(screen)));
	}

	public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent evt) {
		CBCClientCommon.registerClientReloadListeners((listener, id) -> evt.registerReloadListener(listener));
	}

	public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent evt) {
		CBCClientCommon.registerOverlays("hotbar", (id, overlay) -> wrapOverlay(id, overlay, VanillaGuiOverlay.HOTBAR, evt));
		CBCClientCommon.registerOverlays("helmet", (id, overlay) -> wrapOverlay(id, overlay, VanillaGuiOverlay.HELMET, evt));
	}

	public static void onRegisterShaders(RegisterShadersEvent evt) {
		try {
			CBCRenderTypes.registerAllShaders(evt::registerShader, ShaderInstance::new, evt.getResourceProvider());
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload Create Big Cannons shaders: ", e);
		}
	}

}
