package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public class ClientboundNotifyTagReloadPacket implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNotifyTagReloadPacket> STREAM_CODEC = StreamCodec.unit(new ClientboundNotifyTagReloadPacket());

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.reloadTagDependentClientResources(this));
	}

}
