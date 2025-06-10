package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundAnimateCannonContraptionPacket(int id) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAnimateCannonContraptionPacket> STREAM_CODEC =
        ByteBufCodecs.VAR_INT.<RegistryFriendlyByteBuf>cast().map(ClientboundAnimateCannonContraptionPacket::new, ClientboundAnimateCannonContraptionPacket::id);

	public static ClientboundAnimateCannonContraptionPacket entity(AbstractContraptionEntity entity) {
		return new ClientboundAnimateCannonContraptionPacket(entity.getId());
	}

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.animateCannon(this));
	}

}
