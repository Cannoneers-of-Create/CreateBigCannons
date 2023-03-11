package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public class ClientboundAnimateCannonContraptionPacket implements RootPacket {

	private final int id;

	public ClientboundAnimateCannonContraptionPacket(AbstractContraptionEntity entity) {
		this.id = entity.getId();
	}

	public ClientboundAnimateCannonContraptionPacket(FriendlyByteBuf buf) {
		this.id = buf.readVarInt();
	}

	@Override public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.id);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		CBCClientHandlers.animateCannon(this);
	}

	public int id() { return this.id; }

}
