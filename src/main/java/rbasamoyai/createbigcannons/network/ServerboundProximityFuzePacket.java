package rbasamoyai.createbigcannons.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;

public class ServerboundProximityFuzePacket {

	private final int distance;
	
	public ServerboundProximityFuzePacket(int distance) {
		this.distance = distance;
	}
	
	public ServerboundProximityFuzePacket(FriendlyByteBuf buf) {
		this.distance = buf.readVarInt();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.distance);
	}
	
	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			if (ctx.getSender().containerMenu instanceof ProximityFuzeContainer ct) ct.setDistance(this.distance);
		});
		ctx.setPacketHandled(true);
	}
	
}
