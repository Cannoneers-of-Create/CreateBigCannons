package rbasamoyai.createbigcannons.forge.multiloader.forge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import rbasamoyai.createbigcannons.forge.network.CBCNetworkForge;
import rbasamoyai.createbigcannons.forge.network.ForgeClientPacket;
import rbasamoyai.createbigcannons.forge.network.ForgeServerPacket;
import rbasamoyai.createbigcannons.network.RootPacket;

public class NetworkPlatformImpl {

	public static void sendToServer(RootPacket pkt) {
		CBCNetworkForge.INSTANCE.sendToServer(new ForgeServerPacket(pkt));
	}

	public static void sendToClientPlayer(RootPacket pkt, ServerPlayer player) {
		CBCNetworkForge.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ForgeClientPacket(pkt));
	}

	public static void sendToClientTracking(RootPacket pkt, Entity tracked) {
		CBCNetworkForge.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> tracked), new ForgeClientPacket(pkt));
	}

	public static void sendToClientAll(RootPacket pkt, MinecraftServer server) {
		CBCNetworkForge.INSTANCE.send(PacketDistributor.ALL.noArg(), new ForgeClientPacket(pkt));
	}

}
