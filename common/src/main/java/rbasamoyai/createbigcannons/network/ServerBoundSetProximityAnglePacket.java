package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.Nullable;

import rbasamoyai.createbigcannons.base.SimpleValueContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;

import java.util.concurrent.Executor;

public class ServerBoundSetProximityAnglePacket implements RootPacket {
	private final int value;

	public ServerBoundSetProximityAnglePacket(int value){
		this.value = value;
	}

	public ServerBoundSetProximityAnglePacket(FriendlyByteBuf buf) {
		this.value = buf.readVarInt();
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.value);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender != null && sender.containerMenu instanceof ProximityFuzeContainer ct) ct.setAngleValue(this.value);
	}
}
