package rbasamoyai.createbigcannons.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeContainer;

public class ServerboundTimedFuzePacket {

	private final int time;
	
	public ServerboundTimedFuzePacket(int time) {
		this.time = time;
	}
	
	public ServerboundTimedFuzePacket(FriendlyByteBuf buf) {
		this.time = buf.readVarInt();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.time);
	}
	
	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			if (ctx.getSender().containerMenu instanceof TimedFuzeContainer ct) ct.setTime(this.time);
		});
		ctx.setPacketHandled(true);
	}
	
}
