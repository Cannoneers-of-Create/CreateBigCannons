package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record ClientboundPlayBlockHitEffectPacket(BlockState blockState, EntityType<?> entityType, boolean deflect,
												  boolean forceDisplay, double x, double y, double z, float dx, float dy,
												  float dz)
	implements RootPacket {

	public ClientboundPlayBlockHitEffectPacket(FriendlyByteBuf buf) {
		this(Block.stateById(buf.readVarInt()), CBCRegistryUtils.getEntityType(buf.readVarInt()), buf.readBoolean(),
			buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeVarInt(Block.getId(this.blockState))
			.writeVarInt(CBCRegistryUtils.getEntityTypeNumericId(this.entityType))
			.writeBoolean(this.deflect)
			.writeBoolean(this.forceDisplay)
			.writeDouble(this.x)
			.writeDouble(this.y)
			.writeDouble(this.z)
			.writeFloat(this.dx)
			.writeFloat(this.dy)
			.writeFloat(this.dz);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.playBlockHitEffect(this));
	}

}
