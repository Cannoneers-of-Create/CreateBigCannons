package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record ClientboundBlastSoundPacket(SoundEvent sound, SoundSource source, double x, double y, double z, float volume,
										  float pitch, float airAbsorption) implements RootPacket {

	public ClientboundBlastSoundPacket(FriendlyByteBuf buf) {
		this(CBCRegistryUtils.getSoundEvent(buf.readVarInt()), buf.readEnum(SoundSource.class), buf.readDouble(), buf.readDouble(),
			buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(CBCRegistryUtils.getSoundEventNumericId(this.sound))
			.writeEnum(this.source)
			.writeDouble(this.x)
			.writeDouble(this.y)
			.writeDouble(this.z)
			.writeFloat(this.volume)
			.writeFloat(this.pitch)
			.writeFloat(this.airAbsorption);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.playBlastSound(this));
	}

}
