package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.base.SimpleValueContainer;

public record ServerboundSetContainerValuePacket(int value) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetContainerValuePacket> STREAM_CODEC = ByteBufCodecs.VAR_INT.<RegistryFriendlyByteBuf>cast()
        .map(ServerboundSetContainerValuePacket::new, ServerboundSetContainerValuePacket::value);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		if (player.containerMenu instanceof SimpleValueContainer ct)
            ct.setValue(this.value);
	}

}
