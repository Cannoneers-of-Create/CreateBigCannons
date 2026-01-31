package rbasamoyai.createbigcannons.multiloader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import rbasamoyai.createbigcannons.network.CBCNeoForgePacket;
import rbasamoyai.createbigcannons.network.RootPacket;

public class NetworkPlatform {

	@OnlyIn(Dist.CLIENT)
	public static void sendToServer(RootPacket pkt) {
		PacketDistributor.sendToServer(new CBCNeoForgePacket(pkt));
	}

	public static void sendToClientPlayer(RootPacket pkt, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new CBCNeoForgePacket(pkt));
	}

	public static void sendToClientTracking(RootPacket pkt, Entity tracked) {
        PacketDistributor.sendToPlayersTrackingEntity(tracked, new CBCNeoForgePacket(pkt));
	}

	public static void sendToClientAll(RootPacket pkt, MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(new CBCNeoForgePacket(pkt));
	}

}
