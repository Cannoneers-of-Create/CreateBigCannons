package rbasamoyai.createbigcannons;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.network.RootPacket;

public class CBCExpectPlatform {

    @ExpectPlatform public static String platformName() { throw new AssertionError(); }

    @ExpectPlatform public static boolean isFakePlayer(Player player) { throw new AssertionError(); }

    @ExpectPlatform public static void sendToServer(RootPacket pkt) { throw new AssertionError(); }

    @ExpectPlatform public static void sendToClientPlayer(RootPacket pkt, ServerPlayer player) { throw new AssertionError(); }

    @ExpectPlatform public static void sendToClientTracking(RootPacket pkt, Entity tracked) { throw new AssertionError(); }

    @ExpectPlatform public static void sendToClientAll(RootPacket pkt, MinecraftServer server) { throw new AssertionError(); }

}
