package rbasamoyai.createbigcannons;

import java.io.IOException;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import rbasamoyai.createbigcannons.compat.curios.CBCCuriosRenderers;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCRenderTypes;

@Mod(value = CreateBigCannons.MOD_ID, dist = Dist.CLIENT)
public class CBCClientNeoForge {

	public CBCClientNeoForge(IEventBus modEventBus) {
		CBCBlockPartials.init();

		modEventBus.addListener(CBCClientNeoForge::onClientSetup);
		modEventBus.addListener(CBCClientNeoForge::onRegisterKeyMappings);
		modEventBus.addListener(CBCClientNeoForge::onRegisterParticleFactories);
		modEventBus.addListener(CBCClientNeoForge::onLoadComplete);
		modEventBus.addListener(CBCClientNeoForge::onRegisterClientReloadListeners);
		modEventBus.addListener(CBCClientNeoForge::onRegisterGuiOverlays);
		modEventBus.addListener(CBCClientNeoForge::onRegisterShaders);

        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
		forgeEventBus.addListener(CBCClientNeoForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientNeoForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientNeoForge::onFovModify);
		forgeEventBus.addListener(CBCClientNeoForge::onPlayerRenderPre);
        forgeEventBus.addListener(CBCClientNeoForge::onPlayerRenderPost);
		forgeEventBus.addListener(CBCClientNeoForge::onSetupCamera);
		forgeEventBus.addListener(CBCClientNeoForge::onPlayerLogOut);
		forgeEventBus.addListener(CBCClientNeoForge::onClickMouse);
		forgeEventBus.addListener(CBCClientNeoForge::onLoadClientLevel);
		forgeEventBus.addListener(CBCClientNeoForge::onPlayerLogIn);
		forgeEventBus.addListener(CBCClientNeoForge::onPlayerChangeDimension);

		CBCModsNeoForge.CURIOS.executeIfInstalled(() -> () -> CBCCuriosRenderers.register(modEventBus, forgeEventBus));
	}

    // TODO c6 playtest
	private static void wrapOverlay(String id, LayeredDraw.Layer layer, ResourceLocation renderOver, RegisterGuiLayersEvent evt) {
		evt.registerAbove(renderOver, CreateBigCannons.resource(id), layer);
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

	public static void onClientGameTick(ClientTickEvent.Pre evt) { // TODO c6 playtest, see if Pre or Post
		CBCClientCommon.onClientGameTick(Minecraft.getInstance());
	}

	public static void onClickMouse(InputEvent.InteractionKeyMappingTriggered evt) {
		if (CBCClientCommon.onClickMouse(evt.getKeyMapping())) evt.setCanceled(true);
	}

	public static void onScrollMouse(InputEvent.MouseScrollingEvent evt) {
		if (CBCClientCommon.onScrollMouse(Minecraft.getInstance(), evt.getScrollDeltaY())) {
			evt.setCanceled(true);
		}
	}

	public static void onFovModify(ComputeFovModifierEvent evt) {
		evt.setNewFovModifier(CBCClientCommon.onFovModify(Minecraft.getInstance(), evt.getNewFovModifier()));
	}

	public static void onPlayerRenderPre(RenderPlayerEvent.Pre evt) {
		CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), evt.getEntity(), evt.getPartialTick());
	}

    public static void onPlayerRenderPost(RenderPlayerEvent.Post evt) {
        CBCClientCommon.onPlayerRenderPost(evt.getPoseStack(), evt.getEntity(), evt.getPartialTick());
    }

	public static void onSetupCamera(ViewportEvent.ComputeCameraAngles evt) {
        CBCClientCommon.onCameraSetup(evt.getCamera(), evt.getPartialTick(), evt::getYaw, evt::getPitch, evt::getRoll, evt::setYaw, evt::setPitch, evt::setRoll);
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
        container.registerExtensionPoint(IConfigScreenFactory.class, (modctr, screen) -> new BaseConfigScreen(screen, CreateBigCannons.MOD_ID));
	}

	public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent evt) {
		CBCClientCommon.registerClientReloadListeners((listener, id) -> evt.registerReloadListener(listener));
	}

	public static void onRegisterGuiOverlays(RegisterGuiLayersEvent evt) {
		CBCClientCommon.registerOverlays("hotbar", (id, overlay) -> wrapOverlay(id, overlay, VanillaGuiLayers.HOTBAR, evt));
		CBCClientCommon.registerOverlays("helmet", (id, overlay) -> wrapOverlay(id, overlay, VanillaGuiLayers.CAMERA_OVERLAYS, evt));
	}

	public static void onRegisterShaders(RegisterShadersEvent evt) {
		try {
			CBCRenderTypes.registerAllShaders(evt::registerShader, ShaderInstance::new, evt.getResourceProvider());
		} catch (IOException e) {
			throw new RuntimeException("Failed to reload Create Big Cannons shaders: ", e);
		}
	}

}
