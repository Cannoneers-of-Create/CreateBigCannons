package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

public class ServerboundProximityFuzePacket implements RootPacket {

	private final int distance;
	
	public ServerboundProximityFuzePacket(int distance) {
		this.distance = distance;
	}
	
	public ServerboundProximityFuzePacket(FriendlyByteBuf buf) {
		this.distance = buf.readVarInt();
	}
	
	@Override public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.distance);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender != null && sender.containerMenu instanceof ProximityFuzeContainer ct) ct.setDistance(this.distance);
	}
	
}
