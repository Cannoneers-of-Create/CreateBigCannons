package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock<T extends FuzedBlockEntity> extends ProjectileBlock implements ITE<T> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}

	protected static ItemStack getFuze(BlockEntity blockEntity) {
		return blockEntity instanceof FuzedBlockEntity fuzed ? fuzed.getItem(0) : ItemStack.EMPTY;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		if (result.getDirection() != state.getValue(FACING)) return InteractionResult.PASS;

		return this.onTileEntityUse(level, pos, be -> {
			ItemStack stack1 = be.getItem(0);
			if (stack.isEmpty() && !stack1.isEmpty()) {
				if (!level.isClientSide) {
					player.addItem(be.removeItem(0, 1));
					be.notifyUpdate();
				}
				level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
				return InteractionResult.sidedSuccess(level.isClientSide);
			} else if (stack1.isEmpty() && stack.getItem() instanceof FuzeItem) {
				if (!level.isClientSide) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					be.setItem(0, copy);
					if (!player.getAbilities().instabuild) {
						ItemStack copy1 = stack.copy();
						copy1.shrink(1);
						player.setItemInHand(hand, copy1);
					}
					be.notifyUpdate();
				}
				level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.PASS;
		});
	}
	
}
