package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

public interface RootPacket {

	void rootEncode(FriendlyByteBuf buf);
	void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender);

}
