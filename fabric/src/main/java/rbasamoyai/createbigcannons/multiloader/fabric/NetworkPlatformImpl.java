package rbasamoyai.createbigcannons.multiloader.fabric;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.network.CBCNetworkFabric;
import rbasamoyai.createbigcannons.network.FabricPacket;
import rbasamoyai.createbigcannons.network.RootPacket;

public class NetworkPlatformImpl {

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
