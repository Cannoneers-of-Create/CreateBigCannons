package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;

public record ClientboundFluidBlobStackSyncPacket(EndFluidStack fstack, int entityId) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundFluidBlobStackSyncPacket> STREAM_CODEC = StreamCodec.composite(
        EndFluidStack.STREAM_CODEC, ClientboundFluidBlobStackSyncPacket::fstack,
        ByteBufCodecs.VAR_INT, ClientboundFluidBlobStackSyncPacket::entityId,
        ClientboundFluidBlobStackSyncPacket::new);

	public static ClientboundFluidBlobStackSyncPacket entity(FluidBlobBurst blobBurst) {
		return new ClientboundFluidBlobStackSyncPacket(blobBurst.getFluidStack(), blobBurst.getId());
	}

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateFluidBlob(this));
	}

}
