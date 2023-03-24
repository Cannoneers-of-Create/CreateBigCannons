package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.function.Supplier;

public class ForgeServerPacket {

	private final RootPacket pkt;

	public ForgeServerPacket(RootPacket pkt) { this.pkt = pkt; }

	public ForgeServerPacket(FriendlyByteBuf buf) {
		this.pkt = CBCRootNetwork.constructPacket(buf, buf.readVarInt());
	}

	public void encode(FriendlyByteBuf buf) {
		CBCRootNetwork.writeToBuf(this.pkt, buf);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			ServerPlayer sender = ctx.getSender();
			this.pkt.handle(sender.getServer(), ctx.getNetworkManager().getPacketListener(), sender);
		});
		ctx.setPacketHandled(true);
	}

}
