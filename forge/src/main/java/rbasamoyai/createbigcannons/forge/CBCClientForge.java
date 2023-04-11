package rbasamoyai.createbigcannons.forge;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);
		forgeEventBus.addListener(CBCClientForge::registerBindings);

		forgeEventBus.addListener(CBCClientForge::getFogColor);
		forgeEventBus.addListener(CBCClientForge::getFogDensity);
		forgeEventBus.addListener(CBCClientForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientForge::onFovModify);
		forgeEventBus.addListener(CBCClientForge::onPlayerRenderPre);
	}

	public static void onRegisterParticleFactories(RegisterParticleProvidersEvent event) {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCClientCommon.onClientSetup();
	}

	public static void registerBindings(RegisterKeyMappingsEvent event) {
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

	public static void onScrollMouse(InputEvent.MouseScrollingEvent evt) {
		if (CBCClientCommon.onScrollMouse(Minecraft.getInstance(), evt.getScrollDelta()) && evt.isCancelable()) {
			evt.setCanceled(true);
		}
	}

	public static void onFovModify(ComputeFovModifierEvent evt) {
		evt.setNewFovModifier(CBCClientCommon.onFovModify(Minecraft.getInstance(), evt.getFovModifier()));
	}

	public static void onPlayerRenderPre(RenderPlayerEvent.Pre evt) {
		CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), evt.getEntity(), evt.getPartialTick());
	}

}
