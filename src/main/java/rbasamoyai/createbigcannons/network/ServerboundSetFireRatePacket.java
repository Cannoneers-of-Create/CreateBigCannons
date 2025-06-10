package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;

public record ServerboundSetFireRatePacket(int fireRateAdjustment) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetFireRatePacket> STREAM_CODEC = ByteBufCodecs.VAR_INT.<RegistryFriendlyByteBuf>cast()
        .map(ServerboundSetFireRatePacket::new, ServerboundSetFireRatePacket::fireRateAdjustment);

    @Override
    public void handle(Executor exec, PacketListener listener, Player player) {
        if (this.fireRateAdjustment != 0 && player.getRootVehicle() instanceof CannonCarriageEntity carriage)
            carriage.trySettingFireRateCarriage(this.fireRateAdjustment);
    }

}
