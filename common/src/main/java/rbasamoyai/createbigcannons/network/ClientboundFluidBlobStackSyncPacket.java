package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;

public record ClientboundFluidBlobStackSyncPacket(EndFluidStack fstack, int entityId) implements RootPacket {

	public ClientboundFluidBlobStackSyncPacket(FriendlyByteBuf buf) {
		this(EndFluidStack.readBuf(buf), buf.readVarInt());
	}

	public ClientboundFluidBlobStackSyncPacket(FluidBlobBurst blobBurst) {
		this(blobBurst.getFluidStack(), blobBurst.getId());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		this.fstack.writeBuf(buf);
		buf.writeVarInt(this.entityId);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateFluidBlob(this));
	}

}
