package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundNotifyTagReloadPacket() implements RootPacket {

	public ClientboundNotifyTagReloadPacket(FriendlyByteBuf buf) { this(); }

	@Override public void rootEncode(FriendlyByteBuf buf) {}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.reloadTagDependentClientResources(this));
	}

}
