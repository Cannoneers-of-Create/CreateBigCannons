package rbasamoyai.createbigcannons.network;

import java.util.List;
import java.util.concurrent.Executor;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ClientboundFluidExplodePacket(double x, double y, double z, float blockRadius, float entityRadius,
                                            List<BlockPos> toBlow, float knockbackX, float knockbackY, float knockbackZ, Fluid fluid) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundFluidExplodePacket> STREAM_CODEC = CBCStreamCodecs.composite(
        ByteBufCodecs.DOUBLE, ClientboundFluidExplodePacket::x,
        ByteBufCodecs.DOUBLE, ClientboundFluidExplodePacket::y,
        ByteBufCodecs.DOUBLE, ClientboundFluidExplodePacket::z,
        ByteBufCodecs.FLOAT, ClientboundFluidExplodePacket::blockRadius,
        ByteBufCodecs.FLOAT, ClientboundFluidExplodePacket::entityRadius,
        CatnipStreamCodecBuilders.list(BlockPos.STREAM_CODEC), ClientboundFluidExplodePacket::toBlow,
        ByteBufCodecs.FLOAT, ClientboundFluidExplodePacket::knockbackX,
        ByteBufCodecs.FLOAT, ClientboundFluidExplodePacket::knockbackY,
        ByteBufCodecs.FLOAT, ClientboundFluidExplodePacket::knockbackZ,
        CatnipStreamCodecs.FLUID, ClientboundFluidExplodePacket::fluid,
        ClientboundFluidExplodePacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.addFluidExplosionFromServer(this));
	}

	public ClientboundCBCExplodePacket asCBCExplodePacket() {
		return new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.blockRadius, this.entityRadius, this.toBlow,
            this.knockbackX, this.knockbackY, this.knockbackZ, ClientboundCBCExplodePacket.ExplosionType.SHRAPNEL);
	}

}
