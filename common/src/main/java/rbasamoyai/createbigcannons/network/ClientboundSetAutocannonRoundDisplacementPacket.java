package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;

public record ClientboundSetAutocannonRoundDisplacementPacket(int entityId, double displacement) implements RootPacket {

	public ClientboundSetAutocannonRoundDisplacementPacket(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readDouble());
	}

	public ClientboundSetAutocannonRoundDisplacementPacket(AbstractAutocannonProjectile round) {
		this(round.getId(), round.getTotalDisplacement());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId)
			.writeDouble(this.displacement);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.setAutocannonRoundDisplacement(this));
	}

}
