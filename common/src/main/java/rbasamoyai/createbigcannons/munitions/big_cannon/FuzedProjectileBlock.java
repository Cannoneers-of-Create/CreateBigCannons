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
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock<BLOCK_ENTITY extends FuzedBlockEntity, ENTITY extends FuzedBigCannonProjectile>
	extends ProjectileBlock<ENTITY> implements IBE<BLOCK_ENTITY> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}

	public static ItemStack getFuzeFromItemStack(ItemStack stack) {
		return ItemStack.of(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Fuze"));
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		FuzedBigCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlocks(projectileBlocks));
		projectile.setFuze(getFuzeFromBlocks(projectileBlocks));
		return projectile;
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		FuzedBigCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromItemStack(itemStack));
		projectile.setFuze(getFuzeFromItemStack(itemStack));
		return projectile;
	}

	protected static ItemStack getFuzeFromBlocks(List<StructureBlockInfo> blocks) {
		if (blocks.isEmpty()) return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null) return ItemStack.EMPTY;
		BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
		return load instanceof FuzedBlockEntity fuzed ? fuzed.getItem(1) : ItemStack.EMPTY;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (hand == InteractionHand.OFF_HAND)
			return InteractionResult.PASS;
		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
		if (fuzedBlock == null)
			return InteractionResult.PASS;
		ItemStack stack = player.getItemInHand(hand);
		Direction fuzeFace = state.getValue(FACING);
		if (this.isBaseFuze())
			fuzeFace = fuzeFace.getOpposite();
		if (stack.isEmpty()) {
			int slot;
			if (!fuzedBlock.getItem(0).isEmpty()) {
				slot = 0;
			} else if (result.getDirection() == fuzeFace && !fuzedBlock.getItem(1).isEmpty()) {
				slot = 1;
			} else {
				return InteractionResult.PASS;
			}
			if (!level.isClientSide) {
				ItemStack resultStack = fuzedBlock.removeItem(slot, 1);
				if (!player.addItem(resultStack) && !player.isCreative()) {
					ItemEntity item = player.drop(resultStack, false);
					if (item != null) {
						item.setNoPickUpDelay();
						item.setTarget(player.getUUID());
					}
				}
				fuzedBlock.notifyUpdate();
			}
			level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			int slot;
			if (CBCItems.TRACER_TIP.isIn(stack)) {
				slot = 0;
			} else if (stack.getItem() instanceof FuzeItem && result.getDirection() == fuzeFace) {
				slot = 1;
			} else {
				return InteractionResult.PASS;
			}
			if (!fuzedBlock.getItem(slot).isEmpty())
				return InteractionResult.PASS;
			if (!level.isClientSide) {
				ItemStack copy = player.getAbilities().instabuild ? stack.copy() : stack.split(1);
				copy.setCount(1);
				fuzedBlock.setItem(slot, copy);
				fuzedBlock.notifyUpdate();
			}
			level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public abstract boolean isBaseFuze();

}
