package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import java.util.concurrent.Executor;

public record ClientboundCheckChannelVersionPacket(String serverVersion) implements RootPacket {

	public ClientboundCheckChannelVersionPacket(FriendlyByteBuf buf) {
		this(buf.readUtf());
	}


	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeUtf(this.serverVersion);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.checkVersion(this));
	}

}
