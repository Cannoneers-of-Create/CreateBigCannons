package rbasamoyai.createbigcannons.munitions;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock<T extends FuzedBlockEntity> extends ProjectileBlock implements ITE<T> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}

	protected static ItemStack getFuze(BlockEntity blockEntity) {
		if (blockEntity == null || !blockEntity.getBlockState().hasProperty(FACING)) return ItemStack.EMPTY;
		Direction facing = blockEntity.getBlockState().getValue(FACING);
		LazyOptional<IItemHandler> items = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		return items.lazyMap(h -> h.getStackInSlot(0)).lazyMap(ItemStack::copy).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return this.onTileEntityUse(level, pos, be -> {
			if (!level.isClientSide) {
				be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, result.getDirection()).ifPresent(h -> {
					if (player.getItemInHand(hand).isEmpty() && !h.getStackInSlot(0).isEmpty()) {
						player.addItem(h.extractItem(0, 1, false));
						level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
					} else if (h.getStackInSlot(0).isEmpty() && player.getItemInHand(hand).getItem() instanceof FuzeItem) {
						ItemStack itemResult = h.insertItem(0, player.getItemInHand(hand), false);
						if (!player.getAbilities().instabuild) player.setItemInHand(hand, itemResult);
						level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
					}
				});
				be.setChanged();
				be.sendData();
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		});
	}
	
}
