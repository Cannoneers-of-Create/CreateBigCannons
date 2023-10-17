package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class FluidShellBlock extends FuzedProjectileBlock<AbstractFluidShellBlockEntity> {

	public FluidShellBlock(Properties properties) {
		super(properties);
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
		projectile.setFuze(getFuze(projectileBlocks));
		if (!projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (info.nbt != null) {
				BlockEntity load = BlockEntity.loadStatic(info.pos, info.state, info.nbt);
				if (load instanceof AbstractFluidShellBlockEntity shell) shell.setFluidShellStack(projectile);
			}
		}

		return projectile;
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.FLUID_SHELL.get(); }

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		Direction facing = hit.getDirection();
		if (facing != state.getValue(FACING) || hand == InteractionHand.OFF_HAND) return InteractionResult.PASS;

		return this.onBlockEntityUse(level, pos, shell -> {
			if (!stack.isEmpty()) {
				if (shell.tryEmptyItemIntoTE(level, player, hand, stack, facing)) return InteractionResult.SUCCESS;
				if (shell.tryFillItemFromTE(level, player, hand, stack, facing)) return InteractionResult.SUCCESS;
			}
			return super.use(state, level, pos, player, hand, hit);
		});
	}

}
