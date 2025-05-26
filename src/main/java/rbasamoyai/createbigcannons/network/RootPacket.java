package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public interface RootPacket {

	void rootEncode(RegistryFriendlyByteBuf buf);
	void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender);

}
