package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

public class ServerboundFiringActionPacket implements RootPacket {

    public ServerboundFiringActionPacket() {}
    public ServerboundFiringActionPacket(FriendlyByteBuf buf) {}
    @Override public void rootEncode(FriendlyByteBuf buf) {}

    @Override
    public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
        if (sender == null) return;
        Entity rootVehicle = sender.getRootVehicle();
        if (rootVehicle instanceof PitchOrientedContraptionEntity poce) poce.tryFiringShot();
        if (rootVehicle instanceof CannonCarriageEntity carriage) carriage.tryFiringShot();
    }

}
