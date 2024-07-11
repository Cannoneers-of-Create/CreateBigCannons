package rbasamoyai.createbigcannons.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
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

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();

		CBCClientCommon.registerOverlays("hotbar", (id, overlay) -> wrapOverlay(id, overlay, ForgeIngameGui.HOTBAR_ELEMENT));
		CBCClientCommon.registerOverlays("helmet", (id, overlay) -> wrapOverlay(id, overlay, ForgeIngameGui.HELMET_ELEMENT));

		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);
		modEventBus.addListener(CBCClientForge::onTextureStitchAtlasPre);
		modEventBus.addListener(CBCClientForge::onLoadComplete);
		modEventBus.addListener(CBCClientForge::onRegisterClientReloadListeners);

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

	private static void wrapOverlay(String id, CBCClientCommon.CBCGuiOverlay overlay, IIngameOverlay renderOver) {
		OverlayRegistry.registerOverlayAbove(renderOver, id, (gui, stack, partialTicks, width, height) -> {
			overlay.renderOverlay(stack, partialTicks, width, height);
		});
	}

	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(ClientRegistry::registerKeyBinding);
	}

	public static void getFogColor(EntityViewRenderEvent.FogColors event) {
		CBCClientCommon.setFogColor(event.getCamera(), (r, g, b) -> {
			event.setRed(r);
			event.setGreen(g);
			event.setBlue(b);
		});
	}

	public static void getFogDensity(EntityViewRenderEvent.RenderFogEvent event) {
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

	public static void onClickMouse(InputEvent.ClickInputEvent evt) {
		if (CBCClientCommon.onClickMouse(evt.getKeyMapping()) && evt.isCancelable()) evt.setCanceled(true);
	}

	public static void onScrollMouse(InputEvent.MouseScrollEvent evt) {
		if (CBCClientCommon.onScrollMouse(Minecraft.getInstance(), evt.getScrollDelta()) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onFovModify(FOVModifierEvent evt) {
		evt.setNewfov(CBCClientCommon.onFovModify(Minecraft.getInstance(), evt.getFov()));
	}

	public static void onPlayerRenderPre(RenderPlayerEvent.Pre evt) {
		if (CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), (AbstractClientPlayer) evt.getPlayer(), evt.getRenderer(), evt.getPartialTick())
			&& evt.isCancelable())
			evt.setCanceled(true);
	}

	public static void onTextureStitchAtlasPre(TextureStitchEvent.Pre evt) {
		CBCClientCommon.onTextureAtlasStitchPre(evt::addSprite);
	}

	public static void onSetupCamera(EntityViewRenderEvent.CameraSetup evt) {
		if (CBCClientCommon.onCameraSetup(evt.getCamera(), evt.getPartialTicks(), evt::getYaw, evt::getPitch, evt::getRoll,
			evt::setYaw, evt::setPitch, evt::setRoll) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onLoadClientLevel(WorldEvent.Load evt) {
		LevelAccessor level = evt.getWorld();
		if (!level.isClientSide())
			return;
		CBCClientCommon.onLoadClientLevel(level);
	}

	public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggedOutEvent evt) {
		CBCClientCommon.onPlayerLogOut(evt.getPlayer());
	}

	public static void onPlayerLogIn(ClientPlayerNetworkEvent.LoggedInEvent evt) {
		CBCClientCommon.onPlayerLogIn(evt.getPlayer());
	}

	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent evt) {
		CBCClientCommon.onChangeDimension(evt.getPlayer());
	}

	public static void onLoadComplete(FMLLoadCompleteEvent evt) {
		ModContainer container = ModList.get()
			.getModContainerById(CreateBigCannons.MOD_ID)
			.orElseThrow(() -> new IllegalStateException("CBC mod container missing on LoadComplete"));
		container.registerExtensionPoint(ConfigGuiFactory.class,
			() -> new ConfigGuiFactory((mc, screen) -> CBCConfigs.createConfigScreen(screen)));
	}

	public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent evt) {
		CBCClientCommon.registerClientReloadListeners((listener, id) -> evt.registerReloadListener(listener));
	}

}
