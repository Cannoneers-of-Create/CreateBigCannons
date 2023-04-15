package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class ClientboundUpdateContraptionPacket implements RootPacket {

	private final int id;
	private final Map<BlockPos, StructureBlockInfo> changes;

	public ClientboundUpdateContraptionPacket(AbstractContraptionEntity entity, Map<BlockPos, StructureBlockInfo> changes) {
		this.id = entity.getId();
		this.changes = changes;
	}

	public ClientboundUpdateContraptionPacket(AbstractContraptionEntity entity, BlockPos pos, StructureBlockInfo info) {
		this(entity, Map.of(pos, info));
	}
	
	public ClientboundUpdateContraptionPacket(FriendlyByteBuf buf) {
		this.id = buf.readVarInt();
		int sz = buf.readVarInt();
		this.changes = new HashMap<>(sz);
		for (int i = 0; i < sz; ++i) {
			BlockPos pos = buf.readBlockPos();
			BlockPos infoPos = buf.readBlockPos();
			BlockState state = Block.BLOCK_STATE_REGISTRY.byId(buf.readVarInt());
			CompoundTag tag = buf.readBoolean() ? buf.readNbt() : null;
			this.changes.put(pos, new StructureBlockInfo(infoPos, state, tag));
		}
	}

	public int id() { return this.id; }

	public Map<BlockPos, StructureBlockInfo> changes() { return this.changes; }

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.id)
		.writeVarInt(this.changes.size());
		for (Map.Entry<BlockPos, StructureBlockInfo> entry : this.changes.entrySet()) {
			StructureBlockInfo info = entry.getValue();
			buf.writeBlockPos(entry.getKey())
			.writeBlockPos(info.pos)
			.writeVarInt(Block.getId(info.state))
			.writeBoolean(info.nbt != null);
			if (info.nbt != null) buf.writeNbt(info.nbt);
		}
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender){
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.updateContraption(this));
	}
	
}
