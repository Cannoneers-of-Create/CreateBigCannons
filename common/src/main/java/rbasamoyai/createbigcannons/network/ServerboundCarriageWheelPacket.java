package rbasamoyai.createbigcannons.network;

import org.joml.Vector4f;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

public class ServerboundCarriageWheelPacket implements RootPacket {

    private final Vector4f state;
    private final int id;

    public ServerboundCarriageWheelPacket(CannonCarriageEntity entity) {
        this.state = entity.getWheelState();
        this.id = entity.getId();
    }

    public ServerboundCarriageWheelPacket(FriendlyByteBuf buf) {
        this.id = buf.readVarInt();
        this.state = new Vector4f(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    @Override
    public void rootEncode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.id)
        .writeFloat(this.state.x())
        .writeFloat(this.state.y())
        .writeFloat(this.state.z())
        .writeFloat(this.state.w());
    }

    @Override
    public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
        if (sender != null && sender.level().getEntity(this.id) instanceof CannonCarriageEntity carriage) carriage.setWheelState(this.state);
    }

}
