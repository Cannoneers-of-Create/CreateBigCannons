package rbasamoyai.createbigcannons.neoforge.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;
import rbasamoyai.createbigcannons.network.RootPacket;

public class ForgeClientPacket {

	private final RootPacket pkt;

	public ForgeClientPacket(RootPacket pkt) { this.pkt = pkt; }

	public ForgeClientPacket(RegistryFriendlyByteBuf buf) {
		this.pkt = CBCRootNetwork.constructPacket(buf, buf.readVarInt());
	}

	public void encode(FriendlyByteBuf buf) {
		CBCRootNetwork.writeToBuf(this.pkt, buf);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			this.pkt.handle(ctx::enqueueWork, ctx.getNetworkManager().getPacketListener(), null);
		});
		ctx.setPacketHandled(true);
	}

}
