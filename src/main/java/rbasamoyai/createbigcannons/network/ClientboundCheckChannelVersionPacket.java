package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundCheckChannelVersionPacket(String serverVersion) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCheckChannelVersionPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, ClientboundCheckChannelVersionPacket::serverVersion,
        ClientboundCheckChannelVersionPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.checkVersion(this));
	}

}
