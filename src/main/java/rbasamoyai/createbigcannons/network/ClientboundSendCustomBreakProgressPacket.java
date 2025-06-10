package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundSendCustomBreakProgressPacket(BlockPos pos, int damage) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSendCustomBreakProgressPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, ClientboundSendCustomBreakProgressPacket::pos,
        ByteBufCodecs.VAR_INT, ClientboundSendCustomBreakProgressPacket::damage,
        ClientboundSendCustomBreakProgressPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.setCustomBlockDamage(this));
	}

}
