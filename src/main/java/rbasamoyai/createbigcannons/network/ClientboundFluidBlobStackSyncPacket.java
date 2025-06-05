package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;

public record ClientboundFluidBlobStackSyncPacket(EndFluidStack fstack, int entityId) implements RootPacket {

	public ClientboundFluidBlobStackSyncPacket(RegistryFriendlyByteBuf buf) {
		this(EndFluidStack.STREAM_CODEC.decode(buf), buf.readVarInt());
	}

	public ClientboundFluidBlobStackSyncPacket(FluidBlobBurst blobBurst) {
		this(blobBurst.getFluidStack(), blobBurst.getId());
	}

	@Override
	public void rootEncode(RegistryFriendlyByteBuf buf) {
        EndFluidStack.STREAM_CODEC.encode(buf, this.fstack);
		buf.writeVarInt(this.entityId);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateFluidBlob(this));
	}

}
