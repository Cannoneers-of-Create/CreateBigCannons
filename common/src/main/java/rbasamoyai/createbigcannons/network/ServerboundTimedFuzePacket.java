package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeContainer;

import java.util.concurrent.Executor;

public class ServerboundTimedFuzePacket implements RootPacket {

	private final int time;
	
	public ServerboundTimedFuzePacket(int time) {
		this.time = time;
	}
	
	public ServerboundTimedFuzePacket(FriendlyByteBuf buf) {
		this.time = buf.readVarInt();
	}
	
	@Override public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.time);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender != null && sender.containerMenu instanceof TimedFuzeContainer ct) ct.setTime(this.time);
	}
	
}
