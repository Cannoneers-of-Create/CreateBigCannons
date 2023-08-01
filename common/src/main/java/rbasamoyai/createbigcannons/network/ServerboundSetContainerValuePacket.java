package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;

import rbasamoyai.createbigcannons.base.SimpleValueContainer;

import java.util.concurrent.Executor;

public class ServerboundSetContainerValuePacket implements RootPacket {

	private final int value;

	public ServerboundSetContainerValuePacket(int value) {
		this.value = value;
	}

	public ServerboundSetContainerValuePacket(FriendlyByteBuf buf) {
		this.value = buf.readVarInt();
	}

	@Override public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.value);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender != null && sender.containerMenu instanceof SimpleValueContainer ct) ct.setValue(this.value);
	}

}
