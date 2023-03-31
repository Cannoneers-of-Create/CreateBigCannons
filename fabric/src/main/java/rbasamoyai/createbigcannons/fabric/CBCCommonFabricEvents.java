package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.event.common.BlockEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

public class CBCCommonFabricEvents {

	public static void register() {
		ServerTickEvents.END_WORLD_TICK.register(CBCCommonFabricEvents::onServerLevelTick);
		ServerPlayConnectionEvents.JOIN.register(CBCCommonFabricEvents::onPlayerLogin);
		ServerPlayConnectionEvents.DISCONNECT.register(CBCCommonFabricEvents::onPlayerLogout);
		ServerWorldEvents.LOAD.register(CBCCommonFabricEvents::onLoadLevel);
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(CBCCommonFabricEvents::onDatapackSync);
		BlockEvents.BLOCK_BREAK.register(CBCCommonFabricEvents::onPlayerBreakBlock);
	}

	public static void onServerLevelTick(ServerLevel level) {
		CBCCommonEvents.serverLevelTickEnd(level);
	}

	public static void onPlayerBreakBlock(BlockEvents.BreakEvent event) {
		CBCCommonEvents.onPlayerBreakBlock(event.getState(), event.getWorld(), event.getPos(), event.getPlayer());
	}

	public static void onPlayerLogin(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		CBCCommonEvents.onPlayerLogin(handler.getPlayer());
	}

	public static void onPlayerLogout(ServerGamePacketListenerImpl handler, MinecraftServer server) {
		CBCCommonEvents.onPlayerLogout(handler.getPlayer());
	}

	public static void onLoadLevel(MinecraftServer server, ServerLevel level) {
		CreateBigCannons.BLOCK_DAMAGE.levelLoaded(level);
		if (level.getServer() != null && level.getServer().overworld() == level) {
			BlockHardnessHandler.loadTags();
		}
	}

	public static void onDatapackSync(MinecraftServer server, CloseableResourceManager resourceManager, boolean success) {
		CBCCommonEvents.onDatapackSync(null);
	}

}
