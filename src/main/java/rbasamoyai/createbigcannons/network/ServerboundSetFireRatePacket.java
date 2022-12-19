package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;

import java.util.function.Supplier;

public class ServerboundSetFireRatePacket {

    private final int fireRateAdjustment;

    public ServerboundSetFireRatePacket(int fireRateAdjustment) {
        this.fireRateAdjustment = fireRateAdjustment;
    }

    public ServerboundSetFireRatePacket(FriendlyByteBuf buf) {
        this.fireRateAdjustment = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.fireRateAdjustment);
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            if (this.fireRateAdjustment != 0 && ctx.getSender().getVehicle() instanceof CannonCarriageEntity carriage) carriage.trySettingFireRateCarriage(this.fireRateAdjustment);
        });
        ctx.setPacketHandled(true);
    }

}
