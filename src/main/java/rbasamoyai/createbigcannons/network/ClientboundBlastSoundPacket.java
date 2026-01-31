package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ClientboundBlastSoundPacket(SoundEvent sound, SoundSource source, double x, double y, double z, float volume,
										  float pitch, float airAbsorption) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlastSoundPacket> STREAM_CODEC = CBCStreamCodecs.composite(
        SoundEvent.DIRECT_STREAM_CODEC, ClientboundBlastSoundPacket::sound,
        CatnipStreamCodecBuilders.ofEnum(SoundSource.class), ClientboundBlastSoundPacket::source,
        ByteBufCodecs.DOUBLE, ClientboundBlastSoundPacket::x,
        ByteBufCodecs.DOUBLE, ClientboundBlastSoundPacket::y,
        ByteBufCodecs.DOUBLE, ClientboundBlastSoundPacket::z,
        ByteBufCodecs.FLOAT, ClientboundBlastSoundPacket::volume,
        ByteBufCodecs.FLOAT, ClientboundBlastSoundPacket::pitch,
        ByteBufCodecs.FLOAT, ClientboundBlastSoundPacket::airAbsorption,
        ClientboundBlastSoundPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.playBlastSound(this));
	}

}
