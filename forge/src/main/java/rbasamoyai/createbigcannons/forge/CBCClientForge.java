package rbasamoyai.createbigcannons.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();

		CBCClientCommon.registerOverlays((id, overlay) -> {
			// TODO: more flexible but concise method specified in common
			OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, id, (gui, stack, partialTicks, width, height) -> {
				overlay.renderOverlay(stack, partialTicks, width, height);
			});
		});

		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterKeyMappings);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);
		modEventBus.addListener(CBCClientForge::onTextureStitchAtlasPre);

		forgeEventBus.addListener(CBCClientForge::getFogColor);
		forgeEventBus.addListener(CBCClientForge::getFogDensity);
		forgeEventBus.addListener(CBCClientForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientForge::onFovModify);
		forgeEventBus.addListener(CBCClientForge::onPlayerRenderPre);
		forgeEventBus.addListener(CBCClientForge::onSetupCamera);
		forgeEventBus.addListener(CBCClientForge::onPlayerLogOut);
		forgeEventBus.addListener(CBCClientForge::onClickMouse);
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
		if (CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), (AbstractClientPlayer) evt.getEntity(), evt.getRenderer(), evt.getPartialTick())
			&& evt.isCancelable())
			evt.setCanceled(true);
	}

	public static void onTextureStitchAtlasPre(TextureStitchEvent.Pre evt) {
		CBCClientCommon.onTextureAtlasStitchPre(evt::addSprite);
	}

	public static void onSetupCamera(ViewportEvent.ComputeCameraAngles evt) {
		if (CBCClientCommon.onCameraSetup(evt.getCamera(), evt.getPartialTick(), evt::getYaw, evt::getPitch, evt::getRoll,
			evt::setYaw, evt::setPitch, evt::setRoll) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onPlayerLogOut(ClientPlayerNetworkEvent.LoggingOut evt) {
		CBCClientCommon.onPlayerLogOut(evt.getPlayer());
	}

	@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static abstract class ClientModBusEvents {
		@SubscribeEvent
		static void onLoadComplete(FMLLoadCompleteEvent event) {
			ModContainer container = ModList.get()
				.getModContainerById(CreateBigCannons.MOD_ID)
				.orElseThrow(() -> new IllegalStateException("CBC mod container missing on LoadComplete"));
			container.registerExtensionPoint(ConfigScreenFactory.class,
				() -> new ConfigScreenFactory((mc, screen) -> CBCConfigs.createConfigScreen(screen)));
		}
	}
}
