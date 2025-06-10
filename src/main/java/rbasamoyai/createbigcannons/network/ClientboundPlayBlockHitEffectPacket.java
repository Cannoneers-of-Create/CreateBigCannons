package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ClientboundPlayBlockHitEffectPacket(BlockState blockState, EntityType<?> entityType, boolean deflect,
												  boolean forceDisplay, double x, double y, double z, float dx, float dy,
												  float dz)
	implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayBlockHitEffectPacket> STREAM_CODEC = CBCStreamCodecs.composite(
        CatnipStreamCodecs.BLOCK_STATE, ClientboundPlayBlockHitEffectPacket::blockState,
        ByteBufCodecs.registry(Registries.ENTITY_TYPE), ClientboundPlayBlockHitEffectPacket::entityType,
        ByteBufCodecs.BOOL, ClientboundPlayBlockHitEffectPacket::deflect,
        ByteBufCodecs.BOOL, ClientboundPlayBlockHitEffectPacket::forceDisplay,
        ByteBufCodecs.DOUBLE, ClientboundPlayBlockHitEffectPacket::x,
        ByteBufCodecs.DOUBLE, ClientboundPlayBlockHitEffectPacket::y,
        ByteBufCodecs.DOUBLE, ClientboundPlayBlockHitEffectPacket::z,
        ByteBufCodecs.FLOAT, ClientboundPlayBlockHitEffectPacket::dx,
        ByteBufCodecs.FLOAT, ClientboundPlayBlockHitEffectPacket::dy,
        ByteBufCodecs.FLOAT, ClientboundPlayBlockHitEffectPacket::dz,
        ClientboundPlayBlockHitEffectPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.playBlockHitEffect(this));
	}

}
