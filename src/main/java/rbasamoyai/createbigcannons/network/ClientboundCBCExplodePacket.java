package rbasamoyai.createbigcannons.network;

import java.util.List;
import java.util.concurrent.Executor;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ClientboundCBCExplodePacket(double x, double y, double z, float blockPower, float entityPower, List<BlockPos> toBlow, float knockbackX,
                                          float knockbackY, float knockbackZ, ExplosionType explosionType) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCBCExplodePacket> STREAM_CODEC = CBCStreamCodecs.composite(
        ByteBufCodecs.DOUBLE, ClientboundCBCExplodePacket::x,
        ByteBufCodecs.DOUBLE, ClientboundCBCExplodePacket::y,
        ByteBufCodecs.DOUBLE, ClientboundCBCExplodePacket::z,
        ByteBufCodecs.FLOAT, ClientboundCBCExplodePacket::blockPower,
        ByteBufCodecs.FLOAT, ClientboundCBCExplodePacket::entityPower,
        CatnipStreamCodecBuilders.list(BlockPos.STREAM_CODEC), ClientboundCBCExplodePacket::toBlow,
        ByteBufCodecs.FLOAT, ClientboundCBCExplodePacket::knockbackX,
        ByteBufCodecs.FLOAT, ClientboundCBCExplodePacket::knockbackY,
        ByteBufCodecs.FLOAT, ClientboundCBCExplodePacket::knockbackZ,
        CatnipStreamCodecBuilders.ofEnum(ExplosionType.class), ClientboundCBCExplodePacket::explosionType,
        ClientboundCBCExplodePacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.addExplosionFromServer(this));
	}

	public enum ExplosionType {
		SHRAPNEL,
		FLAK,
		SMOKE,
		MORTAR_STONE,
		IMPACT,
		SHELL,
		SHELL_NO_EFFECTS
	}

}
