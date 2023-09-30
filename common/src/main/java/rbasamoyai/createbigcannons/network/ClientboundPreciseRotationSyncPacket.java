package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;

import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import javax.annotation.Nullable;

import java.util.concurrent.Executor;

public record ClientboundPreciseRotationSyncPacket(int entityId, float yRot, float xRot) implements RootPacket {

	public ClientboundPreciseRotationSyncPacket(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId).writeFloat(this.yRot).writeFloat(this.xRot);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.syncPreciseRotation(this));
	}

}
