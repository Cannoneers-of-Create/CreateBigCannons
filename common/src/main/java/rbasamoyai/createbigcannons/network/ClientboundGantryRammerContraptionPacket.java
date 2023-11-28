package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.cannonloading.GantryRammerContraptionEntity;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import javax.annotation.Nullable;

import java.util.concurrent.Executor;

public class ClientboundGantryRammerContraptionPacket implements RootPacket {

    int entityID;
    double coord;
    double motion;

    public ClientboundGantryRammerContraptionPacket(int entityID, double coord, double motion) {
        this.entityID = entityID;
        this.coord = coord;
        this.motion = motion;
    }

    public ClientboundGantryRammerContraptionPacket(FriendlyByteBuf buffer) {
        entityID = buffer.readInt();
        coord = buffer.readFloat();
        motion = buffer.readFloat();
    }

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeFloat((float) coord);
		buf.writeFloat((float) motion);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateGantryRammer(this));
	}

	public int id() {
		return this.entityID;
	}
	public double motion() { return this.motion; }
	public double coord() { return this.coord; }
}
