package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderItem;
import rbasamoyai.createbigcannons.crafting.welding.WeldableBlock;

public record ServerboundUseWelderPacket(BlockPos from, BlockPos to) implements RootPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundUseWelderPacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC, ServerboundUseWelderPacket::from,
        BlockPos.STREAM_CODEC, ServerboundUseWelderPacket::to,
        ServerboundUseWelderPacket::new);

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		if (player == null)
            return;
		Level level = player.level();
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof CannonWelderItem))
            return;
		BlockState state = level.getBlockState(this.from);
		if (!CannonWelderItem.weldBlocks(level, this.from, this.to, false))
            return;
		stack.hurtAndBreak(((WeldableBlock) state.getBlock()).weldDamage(), player, EquipmentSlot.MAINHAND);
	}

}
