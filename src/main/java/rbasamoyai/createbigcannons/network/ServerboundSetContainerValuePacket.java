package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.base.SimpleValueContainer;

public class ServerboundSetContainerValuePacket implements RootPacket {

	private final int value;

	public ServerboundSetContainerValuePacket(int value) {
		this.value = value;
	}

	public ServerboundSetContainerValuePacket(FriendlyByteBuf buf) {
		this.value = buf.readVarInt();
	}

	@Override public void rootEncode(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(this.value);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender != null && sender.containerMenu instanceof SimpleValueContainer ct) ct.setValue(this.value);
	}

}
