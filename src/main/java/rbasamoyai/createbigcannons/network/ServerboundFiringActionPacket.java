package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public class ServerboundFiringActionPacket implements RootPacket {

    private static final ServerboundFiringActionPacket INSTANCE = new ServerboundFiringActionPacket();

    private ServerboundFiringActionPacket() {}

    public static ServerboundFiringActionPacket instance() { return INSTANCE; }

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundFiringActionPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public void handle(Executor exec, PacketListener listener, Player player) {
        if (player == null)
            return;
        Entity rootVehicle = player.getRootVehicle();
        if (rootVehicle instanceof PitchOrientedContraptionEntity poce)
            poce.tryFiringShot();
        if (rootVehicle instanceof CannonCarriageEntity carriage)
            carriage.tryFiringShot();
    }

}
