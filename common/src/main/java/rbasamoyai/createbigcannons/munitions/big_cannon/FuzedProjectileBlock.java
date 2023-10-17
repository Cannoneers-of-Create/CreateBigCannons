package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock<T extends FuzedBlockEntity> extends ProjectileBlock implements IBE<T> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}

	protected static ItemStack getFuze(List<StructureBlockInfo> blocks) {
		if (blocks.isEmpty()) return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null) return ItemStack.EMPTY;
		BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
		return load instanceof FuzedBlockEntity fuzed ? fuzed.getItem(0) : ItemStack.EMPTY;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		ItemStack stack = player.getItemInHand(hand);
		Direction fuzeFace = state.getValue(FACING);
		if (this.isBaseFuze()) fuzeFace = fuzeFace.getOpposite();
		if (result.getDirection() != fuzeFace || hand == InteractionHand.OFF_HAND)
			return InteractionResult.PASS;

		return this.onBlockEntityUse(level, pos, be -> {
			ItemStack stack1 = be.getItem(0);
			if (stack.isEmpty() && !stack1.isEmpty()) {
				if (!level.isClientSide) {
					ItemStack resultStack = be.removeItem(0, 1);
					if (!player.addItem(resultStack) && !player.isCreative()) {
						ItemEntity item = player.drop(resultStack, false);
						if (item != null) {
							item.setNoPickUpDelay();
							item.setTarget(player.getUUID());
						}
					}
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

	public boolean isBaseFuze() { return MunitionPropertiesHandler.getProperties(this.getAssociatedEntityType()).baseFuze(); }

}
