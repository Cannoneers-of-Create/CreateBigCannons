package rbasamoyai.createbigcannons;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);

		forgeEventBus.addListener(CBCClientForge::getFogColor);
		forgeEventBus.addListener(CBCClientForge::getFogDensity);
		forgeEventBus.addListener(CBCClientForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientForge::onFovModify);
		forgeEventBus.addListener(CBCClientForge::onPlayerRenderPre);
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
			event.setCanceled(true);
		}
	}

	public static void onClientGameTick(TickEvent.ClientTickEvent evt) {
		CBCClientCommon.onClientGameTick(Minecraft.getInstance());
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
		CBCClientCommon.onPlayerRenderPre(evt.getPoseStack(), evt.getPlayer(), evt.getPartialTick());
	}

}
