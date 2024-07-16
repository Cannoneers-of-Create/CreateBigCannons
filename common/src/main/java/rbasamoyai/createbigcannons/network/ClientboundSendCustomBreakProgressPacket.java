package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundSendCustomBreakProgressPacket(BlockPos pos, int damage) implements RootPacket {

	public ClientboundSendCustomBreakProgressPacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readVarInt());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos)
			.writeVarInt(this.damage);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.setCustomBlockDamage(this));
	}

}
