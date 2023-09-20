package rbasamoyai.createbigcannons.fabric.network;

import com.simibubi.create.foundation.networking.SimplePacketBase;

import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;
import rbasamoyai.createbigcannons.network.RootPacket;

public class FabricPacket extends SimplePacketBase {

	private final RootPacket pkt;

	public FabricPacket(RootPacket pkt) {
		this.pkt = pkt;
	}

	public FabricPacket(FriendlyByteBuf buf) {
		this.pkt = CBCRootNetwork.constructPacket(buf, buf.readVarInt());
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		CBCRootNetwork.writeToBuf(this.pkt, buffer);
	}

	@Override
	public boolean handle(Context context) {
		context.exec().execute(() -> {
			this.pkt.handle(context.exec(), context.listener(), context.sender());
		});
		return true;
	}
}
