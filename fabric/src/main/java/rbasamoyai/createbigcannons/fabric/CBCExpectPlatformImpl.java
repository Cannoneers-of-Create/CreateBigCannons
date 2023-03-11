package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.fake_players.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.fabric.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.fabric.network.FabricPacket;
import rbasamoyai.createbigcannons.network.RootPacket;

public class CBCExpectPlatformImpl {
	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}

	public static boolean isFakePlayer(Player player) {
		return player instanceof FakePlayer;
	}

	public static void sendToServer(RootPacket pkt) {
		CBCNetworkFabric.INSTANCE.sendToServer(new FabricPacket(pkt));
	}

	public static void sendToClientPlayer(RootPacket pkt, ServerPlayer player) {
		CBCNetworkFabric.INSTANCE.sendToClient(new FabricPacket(pkt), player);
	}

	public static void sendToClientTracking(RootPacket pkt, Entity tracked) {
		CBCNetworkFabric.INSTANCE.sendToClientsTracking(new FabricPacket(pkt), tracked);
	}

	public static void sendToClientAll(RootPacket pkt, MinecraftServer server) {
		CBCNetworkFabric.INSTANCE.sendToClientsInServer(new FabricPacket(pkt), server);
	}

}
