package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
		FuzedBigCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlock(level, pos, state));
		projectile.setFuze(getFuzeFromBlock(level, pos, state));
		return projectile;
	}

	@Override
	protected AbstractBigCannonProjectile spawnFromExplosion(Level level, BlockPos pos, BlockState state, Explosion explosion) {
		AbstractBigCannonProjectile projectile = super.spawnFromExplosion(level, pos, state, explosion);
		if (projectile instanceof FuzedBigCannonProjectile fuzedProjectile) {
			fuzedProjectile.setExplosionCountdown(level.random.nextInt(10) + 5);
		}
		return projectile;
	}

	protected static ItemStack getFuzeFromBlocks(List<StructureBlockInfo> blocks) {
		if (blocks.isEmpty()) return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null) return ItemStack.EMPTY;
		BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
		return load instanceof FuzedBlockEntity fuzed ? fuzed.getItem(1) : ItemStack.EMPTY;
	}

	public static ItemStack getFuzeFromBlock(Level level, BlockPos pos, BlockState state) {
		return level.getBlockEntity(pos) instanceof FuzedBlockEntity projectile ? projectile.getFuze() : ItemStack.EMPTY;
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
				if (!level.getBlockTicks().willTickThisTick(pos, this)) {
					level.scheduleTick(pos, this, 0);
				}
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
				if (!level.getBlockTicks().willTickThisTick(pos, this)) {
					level.scheduleTick(pos, this, 0);
				}
			}
			level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
	}

	public abstract boolean isBaseFuze();

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
		if (!level.isClientSide) {
			if (!level.getBlockTicks().willTickThisTick(pos, this)) {
				level.scheduleTick(pos, this, 0);
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
		if (fuzedBlock == null)
			return;
		ItemStack itemStack = fuzedBlock.getFuze();
		if (itemStack.getItem() instanceof FuzeItem fuze) {
			for (Direction dir : Iterate.directions) {
				int signal = level.getSignal(pos.relative(dir), dir);
				if (fuze.onRedstoneSignal(itemStack, level, pos, state, signal, dir)) {
					this.detonateProjectileOnTheSpot(level, pos, state, dir);
					break;
				}
			}
		}
	}

	public void detonateProjectileOnTheSpot(Level level, BlockPos pos, BlockState state, Direction dir) {
		AbstractBigCannonProjectile projectile = this.getProjectile(level, pos, state);
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		if (!(projectile instanceof FuzedBigCannonProjectile fuzedProjectile))
			return;

		Vec3 orientation = new Vec3(dir.step());
		projectile.setOrientation(orientation);
		projectile.setPos(Vec3.atCenterOf(pos));
		projectile.setDeltaMovement(orientation.scale(0.5)); // Velocity boost for burst shells
		fuzedProjectile.detonate(projectile.position());
	}

}
