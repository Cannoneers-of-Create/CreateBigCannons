package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundSyncExtraEntityDataPacket(int entityId, CompoundTag data) implements RootPacket {

	public ClientboundSyncExtraEntityDataPacket(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readNbt());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId)
			.writeNbt(this.data);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.syncExtraEntityData(this));
	}

}
