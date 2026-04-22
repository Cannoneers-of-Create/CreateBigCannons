package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsPacket;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour;
import rbasamoyai.createbigcannons.utils.CBCStreamCodecs;

/**
 * Adapted from {@link ValueSettingsPacket}
 */
public record ServerboundSetFixedCannonMountValuePacket(BlockPos pos, int row, int value, @Nullable InteractionHand interactHand,
                                                        @Nullable BlockHitResult hitResult, Direction side, boolean ctrlDown, boolean pitch) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetFixedCannonMountValuePacket> STREAM_CODEC = CBCStreamCodecs.composite(
        BlockPos.STREAM_CODEC, ServerboundSetFixedCannonMountValuePacket::pos,
        ByteBufCodecs.VAR_INT, ServerboundSetFixedCannonMountValuePacket::row,
        ByteBufCodecs.VAR_INT, ServerboundSetFixedCannonMountValuePacket::value,
        CatnipStreamCodecBuilders.nullable(CatnipStreamCodecBuilders.ofEnum(InteractionHand.class)), ServerboundSetFixedCannonMountValuePacket::interactHand,
        CatnipStreamCodecBuilders.nullable(CatnipStreamCodecs.BLOCK_HIT_RESULT), ServerboundSetFixedCannonMountValuePacket::hitResult,
        Direction.STREAM_CODEC, ServerboundSetFixedCannonMountValuePacket::side,
        ByteBufCodecs.BOOL, ServerboundSetFixedCannonMountValuePacket::ctrlDown,
        ByteBufCodecs.BOOL, ServerboundSetFixedCannonMountValuePacket::pitch,
        ServerboundSetFixedCannonMountValuePacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		// Adapted from BlockEntityConfigurationPacket#handle
        if (player == null)
            return;
        Level level = player.level();
        if (!level.isLoaded(this.pos) || !player.canInteractWithBlock(this.pos, 20))
            return;
        BlockEntity blockEntity = level.getBlockEntity(this.pos);
        if (!(blockEntity instanceof FixedCannonMountBlockEntity mount))
            return;
        this.applySettings(player, mount);
        mount.sendData();
        blockEntity.setChanged();
	}

	private void applySettings(Player player, SmartBlockEntity be) {
		// Adapted from ValueSettingsPacket#applySettings
		BehaviourType<?> type = this.pitch ? FixedCannonMountScrollValueBehaviour.PITCH_TYPE : FixedCannonMountScrollValueBehaviour.YAW_TYPE;
		BlockEntityBehaviour behaviour = be.getBehaviour(type);
		if (!(behaviour instanceof FixedCannonMountScrollValueBehaviour angleBehaviour) || !angleBehaviour.acceptsValueSettings())
			return;
		if (this.interactHand != null) {
			angleBehaviour.onShortInteract(player, this.interactHand, this.side, this.hitResult);
			return;
		}
		angleBehaviour.setValueSettings(player, new ValueSettingsBehaviour.ValueSettings(this.row, this.value), this.ctrlDown);
	}

}
