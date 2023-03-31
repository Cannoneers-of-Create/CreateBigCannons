package rbasamoyai.createbigcannons.forge;

import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;

public class CBCCommonForgeEvents {

	public static void register(IEventBus forgeEventBus) {
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerBreakBlock);
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerLogin);
		forgeEventBus.addListener(CBCCommonForgeEvents::onPlayerLogout);
		forgeEventBus.addListener(CBCCommonForgeEvents::onLoadWorld);
		forgeEventBus.addListener(CBCCommonForgeEvents::onServerWorldTick);
		forgeEventBus.addListener(CBCCommonForgeEvents::onDatapackSync);
	}

	public static void onServerWorldTick(TickEvent.WorldTickEvent evt) {
		if (evt.phase == TickEvent.Phase.START) return;
		if (evt.side == LogicalSide.CLIENT) return;
		CBCCommonEvents.serverLevelTickEnd(evt.world);
	}

	public static void onPlayerBreakBlock(BreakEvent event) {
		CBCCommonEvents.onPlayerBreakBlock(event.getState(), event.getWorld(), event.getPos(), event.getPlayer());
	}

	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent evt) {
		CBCCommonEvents.onPlayerLogin(evt.getPlayer());
	}

	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent evt) {
		CBCCommonEvents.onPlayerLogout(evt.getPlayer());
	}

	public static void onLoadWorld(WorldEvent.Load evt) {
		CBCCommonEvents.onLoadLevel(evt.getWorld());
	}

	public static void onDatapackSync(OnDatapackSyncEvent evt) {
		CBCCommonEvents.onDatapackSync(evt.getPlayer());
	}
	
}
