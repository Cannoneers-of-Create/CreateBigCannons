package rbasamoyai.createbigcannons.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rbasamoyai.createbigcannons.CreateBigCannons;

public record CBCNeoForgePacket(RootPacket pkt) implements CustomPacketPayload {

    public static final Type<CBCNeoForgePacket> TYPE = new Type<>(CreateBigCannons.resource("neoforge_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CBCNeoForgePacket> STREAM_CODEC = CBCRootNetwork.PACKET_STREAM_CODEC
        .map(CBCNeoForgePacket::new, CBCNeoForgePacket::pkt); // TODO c6 playtest

    public static void handlePacket(CBCNeoForgePacket nfPkt, IPayloadContext ctx) {
        nfPkt.pkt.handle(ctx::enqueueWork, ctx.listener(), ctx.player());
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

}
