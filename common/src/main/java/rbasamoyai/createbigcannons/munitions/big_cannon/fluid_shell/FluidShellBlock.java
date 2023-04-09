package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class FluidShellBlock extends FuzedProjectileBlock<AbstractFluidShellBlockEntity> {

	public FluidShellBlock(Properties properties) {
		super(properties);
	}
	
	@Override public Class<AbstractFluidShellBlockEntity> getTileEntityClass() { return AbstractFluidShellBlockEntity.class; }
	@Override public BlockEntityType<? extends AbstractFluidShellBlockEntity> getTileEntityType() { return CBCBlockEntities.FLUID_SHELL.get(); }
	
	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuze(blockEntity));
		if (blockEntity instanceof AbstractFluidShellBlockEntity shell) shell.setFluidShellStack(projectile);
		return projectile;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		Direction facing = hit.getDirection();
		if (facing != state.getValue(FACING)) return InteractionResult.PASS;

		return this.onTileEntityUse(level, pos, shell -> {
			if (!stack.isEmpty()) {
				if (shell.tryEmptyItemIntoTE(level, player, hand, stack, facing)) return InteractionResult.SUCCESS;
				if (shell.tryFillItemFromTE(level, player, hand, stack, facing)) return InteractionResult.SUCCESS;
			}
			return super.use(state, level, pos, player, hand, hit);
		});
	}

}
