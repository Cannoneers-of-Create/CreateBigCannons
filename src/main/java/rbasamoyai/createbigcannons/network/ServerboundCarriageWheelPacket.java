package rbasamoyai.createbigcannons.network;

import com.mojang.math.Vector4f;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;

import java.util.function.Supplier;

public class ServerboundCarriageWheelPacket {

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

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.id)
        .writeFloat(this.state.x())
        .writeFloat(this.state.y())
        .writeFloat(this.state.z())
        .writeFloat(this.state.w());
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender().level.getEntity(this.id) instanceof CannonCarriageEntity carriage) carriage.setWheelState(this.state);
        });
        ctx.setPacketHandled(true);
    }

}
