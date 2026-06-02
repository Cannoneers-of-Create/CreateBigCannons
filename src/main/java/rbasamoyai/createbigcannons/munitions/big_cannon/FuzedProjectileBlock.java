package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.block.IBE;

import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock<BLOCK_ENTITY extends FuzedBlockEntity, ENTITY extends FuzedBigCannonProjectile>
	extends ProjectileBlock<ENTITY> implements IBE<BLOCK_ENTITY> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}

	public static ItemStack getFuzeFromItemStack(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
		return items.copyOne();
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		FuzedBigCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlocks(projectileBlocks, level.registryAccess()));
		projectile.setFuze(getFuzeFromBlocks(projectileBlocks, level.registryAccess()));
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

	protected static ItemStack getFuzeFromBlocks(List<StructureBlockInfo> blocks, HolderLookup.Provider registries) {
		if (blocks.isEmpty()) return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null) return ItemStack.EMPTY;
        return ItemStack.parseOptional(registries, info.nbt().getCompound("Fuze"));
	}

	public static ItemStack getFuzeFromBlock(Level level, BlockPos pos, BlockState state) {
		return level.getBlockEntity(pos) instanceof FuzedBlockEntity projectile ? projectile.getFuze() : ItemStack.EMPTY;
	}

    @Override
    public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation, HolderLookup.Provider registries) {
        StructureBlockInfo info = super.getHandloadingInfo(stack, localPos, cannonOrientation, registries);
        if (info.nbt() != null) {
            ItemStack fuze = getFuzeFromItemStack(stack);
            info.nbt().put("Fuze", fuze.saveOptional(registries));
        }
        return info;
    }

    @Override
    public ItemStack getExtractedItem(StructureBlockInfo info, HolderLookup.Provider registries) {
        ItemStack stack = super.getExtractedItem(info, registries);
        if (info.nbt() != null) {
            ItemStack fuze = ItemStack.parseOptional(registries, info.nbt().getCompound("Fuze"));
            stack.set(CBCDataComponents.FUZE, fuze.isEmpty() ? ItemContainerContents.EMPTY : ItemContainerContents.fromItems(List.of(fuze)));
        }
        return stack;
    }

    @Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
		if (fuzedBlock == null)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		Direction fuzeFace = state.getValue(FACING);
		if (this.isBaseFuze())
			fuzeFace = fuzeFace.getOpposite();
        int slot;
        if (CBCItems.TRACER_TIP.isIn(stack)) {
            slot = 0;
        } else if (stack.getItem() instanceof FuzeItem && hitResult.getDirection() == fuzeFace) {
            slot = 1;
        } else {
            return stack.isEmpty() ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.FAIL;
        }
        if (!fuzedBlock.getItem(slot).isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
        if (fuzedBlock == null)
            return InteractionResult.PASS;
        Direction fuzeFace = state.getValue(FACING);
        if (this.isBaseFuze())
            fuzeFace = fuzeFace.getOpposite();
        int slot;
        if (!fuzedBlock.getItem(0).isEmpty()) {
            slot = 0;
        } else if (hitResult.getDirection() == fuzeFace && !fuzedBlock.getItem(1).isEmpty()) {
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

		Vec3 orientation = CBCCompatTransformers.transformLocationNormal(level, pos, new Vec3(state.getValue(FACING).step()));
		projectile.setOrientation(orientation);
		projectile.setPos(CBCCompatTransformers.transformVec3(level, Vec3.atCenterOf(pos)));
		projectile.setDeltaMovement(orientation.scale(0.5)); // Velocity boost for burst shells
		fuzedProjectile.detonate(projectile.position());
	}

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack item = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof FuzedBlockEntity be) {
            ItemStack fuze = be.getFuze();
            item.set(CBCDataComponents.FUZE, fuze.isEmpty() ? ItemContainerContents.EMPTY : ItemContainerContents.fromItems(List.of(fuze)));
        }
        return item;
    }

    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> serverType) {
        return createTickerHelper(serverType, CBCBlockEntities.FUZED_BLOCK.get(), FuzedProjectileBlock::tickBlockEntity);
    }

    protected static <T extends FuzedBlockEntity> void tickBlockEntity(Level level, BlockPos pos, BlockState state, T blockEntity) {
        blockEntity.tick();
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
        BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

}
