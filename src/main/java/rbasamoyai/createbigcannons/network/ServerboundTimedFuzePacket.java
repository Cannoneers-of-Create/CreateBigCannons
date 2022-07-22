package rbasamoyai.createbigcannons.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
			AbstractContainerMenu ct = ctx.getSender().containerMenu;
			if (!(ct instanceof TimedFuzeContainer)) return;
			((TimedFuzeContainer) ct).setTime(this.time);
		});
		ctx.setPacketHandled(true);
	}
	
}
