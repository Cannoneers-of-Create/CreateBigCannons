package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public class ClientboundAnimateCannonContraptionPacket implements RootPacket {

	private final int id;

	public ClientboundAnimateCannonContraptionPacket(AbstractContraptionEntity entity) {
		this.id = entity.getId();
	}

	public ClientboundAnimateCannonContraptionPacket(FriendlyByteBuf buf) {
		this.id = buf.readVarInt();
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.id);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.animateCannon(this));
	}

	public int id() {
		return this.id;
	}

}
