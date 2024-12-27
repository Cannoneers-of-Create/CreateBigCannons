package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour;

/**
 * Adapted from {@link ValueSettingsPacket}
 */
public record ServerboundSetFixedCannonMountValuePacket(BlockPos pos, int row, int value, @Nullable InteractionHand interactHand,
														Direction side, boolean ctrlDown, boolean pitch) implements RootPacket {

	public ServerboundSetFixedCannonMountValuePacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readVarInt(), buf.readVarInt(),
			buf.readBoolean() ?
				buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND
				: null, Direction.values()[buf.readVarInt()], buf.readBoolean(), buf.readBoolean());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.pos)
			.writeVarInt(this.row)
			.writeVarInt(this.value)
			.writeBoolean(this.interactHand != null);
		if (this.interactHand != null)
			buf.writeBoolean(this.interactHand == InteractionHand.MAIN_HAND);
		buf.writeVarInt(this.side.ordinal())
			.writeBoolean(this.ctrlDown)
			.writeBoolean(this.pitch);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		// Adapted from BlockEntityConfigurationPacket#handle
		exec.execute(() -> {
			if (sender == null)
				return;
			Level level = sender.level();
			if (level == null || !level.isLoaded(this.pos))
				return;
			if (!this.pos.closerThan(sender.blockPosition(), 20))
				return;
			BlockEntity blockEntity = level.getBlockEntity(this.pos);
			if (!(blockEntity instanceof FixedCannonMountBlockEntity mount))
				return;
			this.applySettings(sender, mount);
			mount.sendData();
			blockEntity.setChanged();
		});
	}

	private void applySettings(ServerPlayer player, SmartBlockEntity be) {
		// Adapted from ValueSettingsPacket#applySettings
		BehaviourType<?> type = this.pitch ? FixedCannonMountScrollValueBehaviour.PITCH_TYPE : FixedCannonMountScrollValueBehaviour.YAW_TYPE;
		BlockEntityBehaviour behaviour = be.getBehaviour(type);
		if (!(behaviour instanceof FixedCannonMountScrollValueBehaviour angleBehaviour) || !angleBehaviour.acceptsValueSettings())
			return;
		if (this.interactHand != null) {
			angleBehaviour.onShortInteract(player, this.interactHand, this.side);
			return;
		}
		angleBehaviour.setValueSettings(player, new ValueSettingsBehaviour.ValueSettings(this.row, this.value), this.ctrlDown);
	}

}
