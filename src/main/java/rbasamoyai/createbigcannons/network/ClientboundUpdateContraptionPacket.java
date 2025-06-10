package rbasamoyai.createbigcannons.network;

import java.util.Map;
import java.util.concurrent.Executor;

import com.google.common.collect.Maps;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

public record ClientboundUpdateContraptionPacket(int id, Map<BlockPos, StructureBlockInfo> changes) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateContraptionPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, ClientboundUpdateContraptionPacket::id,
        ByteBufCodecs.map(Maps::newHashMapWithExpectedSize, BlockPos.STREAM_CODEC, CBCStreamCodecs.STRUCTURE_BLOCK_INFO), ClientboundUpdateContraptionPacket::changes,
        ClientboundUpdateContraptionPacket::new); // TODO c6 playtest

	public static ClientboundUpdateContraptionPacket entity(AbstractContraptionEntity entity, Map<BlockPos, StructureBlockInfo> changes) {
        return new ClientboundUpdateContraptionPacket(entity.getId(), changes);
	}

	public static ClientboundUpdateContraptionPacket entity(AbstractContraptionEntity entity, BlockPos pos, StructureBlockInfo info) {
		return new ClientboundUpdateContraptionPacket(entity.getId(), Map.of(pos, info));
	}

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateContraption(this));
	}

}
