package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;

import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import javax.annotation.Nullable;

import java.util.concurrent.Executor;

public record ClientboundAddShakeEffectPacket(int seed, int time, float magnitude) implements RootPacket {

	public ClientboundAddShakeEffectPacket(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt(), buf.readFloat());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.seed).writeVarInt(this.time).writeFloat(this.magnitude);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.addShakeEffect(this));
	}

}
