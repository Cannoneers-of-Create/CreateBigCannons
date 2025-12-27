package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import org.joml.Vector4f;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ServerboundCarriageWheelPacket(Vector4f state, int id) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCarriageWheelPacket> STREAM_CODEC = StreamCodec.composite(
        CBCStreamCodecs.VECTOR_4F, ServerboundCarriageWheelPacket::state,
        ByteBufCodecs.VAR_INT, ServerboundCarriageWheelPacket::id,
        ServerboundCarriageWheelPacket::new);

    public static ServerboundCarriageWheelPacket entity(CannonCarriageEntity entity) {
        return new ServerboundCarriageWheelPacket(entity.getWheelState(), entity.getId());
    }

    @Override
    public void handle(Executor exec, PacketListener listener, Player player) {
        if (player.level().getEntity(this.id) instanceof CannonCarriageEntity carriage)
            carriage.setWheelState(this.state);
    }

}
