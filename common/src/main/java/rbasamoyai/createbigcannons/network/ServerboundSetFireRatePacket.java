package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

public class ServerboundSetFireRatePacket implements RootPacket {

    private final int fireRateAdjustment;

    public ServerboundSetFireRatePacket(int fireRateAdjustment) {
        this.fireRateAdjustment = fireRateAdjustment;
    }

    public ServerboundSetFireRatePacket(FriendlyByteBuf buf) {
        this.fireRateAdjustment = buf.readVarInt();
    }

    @Override public void rootEncode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.fireRateAdjustment);
    }

    @Override
    public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
        if (this.fireRateAdjustment != 0
            && sender != null
            && sender.getRootVehicle() instanceof CannonCarriageEntity carriage)
            carriage.trySettingFireRateCarriage(this.fireRateAdjustment);
    }

}
