package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderItem;
import rbasamoyai.createbigcannons.crafting.welding.WeldableBlock;

import javax.annotation.Nullable;

public record ServerboundUseWelderPacket(BlockPos from, BlockPos to) implements RootPacket {

	public ServerboundUseWelderPacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readBlockPos());
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeBlockPos(this.from).writeBlockPos(this.to);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		if (sender == null) return;
		ServerLevel level = sender.serverLevel();
		ItemStack stack = sender.getMainHandItem();
		if (!(stack.getItem() instanceof CannonWelderItem)) return;
		BlockState state = level.getBlockState(this.from);
		if (!CannonWelderItem.weldBlocks(level, this.from, this.to, false)) return;
		stack.hurtAndBreak(((WeldableBlock) state.getBlock()).weldDamage(), sender, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
	}

}
