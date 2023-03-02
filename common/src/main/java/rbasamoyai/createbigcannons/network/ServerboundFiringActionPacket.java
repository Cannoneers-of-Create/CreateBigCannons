package rbasamoyai.createbigcannons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.function.Supplier;

public class ServerboundFiringActionPacket {

    public ServerboundFiringActionPacket() {}
    public ServerboundFiringActionPacket(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            Entity rootVehicle = ctx.getSender().getRootVehicle();
            if (rootVehicle instanceof PitchOrientedContraptionEntity poce) poce.tryFiringShot();
            if (rootVehicle instanceof CannonCarriageEntity carriage) carriage.tryFiringShot();
        });
        ctx.setPacketHandled(true);
    }

}
