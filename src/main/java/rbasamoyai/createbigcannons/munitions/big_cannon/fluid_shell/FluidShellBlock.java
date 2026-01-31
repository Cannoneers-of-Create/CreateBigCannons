package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class FluidShellBlock extends FuzedProjectileBlock<AbstractFluidShellBlockEntity, FluidShellProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(FluidShellBlock::new);

	public FluidShellBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	public Class<AbstractFluidShellBlockEntity> getBlockEntityClass() {
		return AbstractFluidShellBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AbstractFluidShellBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.FLUID_SHELL.get();
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuzeFromBlocks(projectileBlocks));
		projectile.setTracer(getTracerFromBlocks(projectileBlocks, level.registryAccess()));
		if (!projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (info.nbt() != null) {
				BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt(), level.registryAccess());
				if (load instanceof AbstractFluidShellBlockEntity shell) shell.setFluidShellStack(projectile);
			}
		}
		return projectile;
	}

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuzeFromItemStack(itemStack));
		projectile.setTracer(getTracerFromItemStack(itemStack));
		CompoundTag fluidTag = itemStack.get(CBCDataComponents.FLUID_CONTENT).copyTag();
		projectile.setFluidStack(EndFluidStack.readTag(fluidTag, level.registryAccess()));
		return projectile;
    }

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
		FluidShellProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlock(level, pos, state));
		projectile.setFuze(getFuzeFromBlock(level, pos, state));
		if (level.getBlockEntity(pos) instanceof AbstractFluidShellBlockEntity fluidShell)
			fluidShell.setFluidShellStack(projectile);
		return projectile;
	}

    @Override
	public EntityType<? extends FluidShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.FLUID_SHELL.get();
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		Direction facing = hit.getDirection();
		Direction targetDir = this.isBaseFuze() ? state.getValue(FACING).getOpposite() : state.getValue(FACING);
		boolean correctOrientation = facing == targetDir;

		return this.onBlockEntityUseItemOn(level, pos, shell -> {
			if (!stack.isEmpty() && correctOrientation) {
				if (shell.tryEmptyItemIntoTE(level, player, hand, stack, facing)) return ItemInteractionResult.SUCCESS;
				if (shell.tryFillItemFromTE(level, player, hand, stack, facing)) return ItemInteractionResult.SUCCESS;
			}
			return super.useItemOn(stack, state, level, pos, player, hand, hit);
		});
	}

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return this.onBlockEntityUse(level, pos, shell -> super.useWithoutItem(state, level, pos, player, hit));
    }

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.FLUID_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

}
