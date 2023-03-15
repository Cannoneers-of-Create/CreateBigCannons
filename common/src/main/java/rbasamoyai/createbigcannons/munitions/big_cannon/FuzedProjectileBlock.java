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
		return this.onTileEntityUse(level, pos, be -> {
			if (!level.isClientSide && result.getDirection() == state.getValue(FACING)) {
				ItemStack stack = player.getItemInHand(hand);
				ItemStack stack1 = be.getItem(0);
				if (stack.isEmpty() && !stack1.isEmpty()) {
					player.addItem(be.removeItem(0, 1));
					level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
				} else if (stack1.isEmpty() && stack.getItem() instanceof FuzeItem) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					be.setItem(0, copy);
					if (!player.getAbilities().instabuild) {
						ItemStack copy1 = stack.copy();
						copy.shrink(1);
						player.setItemInHand(hand, copy1);
					}
					level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
				}
				be.setChanged();
				be.sendData();
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		});
	}
	
}
