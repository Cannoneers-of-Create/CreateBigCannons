package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundPreciseRotationSyncPacket(int entityId, float yRot, float xRot) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPreciseRotationSyncPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ClientboundPreciseRotationSyncPacket::entityId,
        ByteBufCodecs.FLOAT, ClientboundPreciseRotationSyncPacket::yRot,
        ByteBufCodecs.FLOAT, ClientboundPreciseRotationSyncPacket::xRot,
        ClientboundPreciseRotationSyncPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.syncPreciseRotation(this));
	}

}
