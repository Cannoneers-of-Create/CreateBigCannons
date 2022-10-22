package rbasamoyai.createbigcannons.munitions.fluidshell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlock;

public class FluidShellBlock extends FuzedProjectileBlock<FluidShellBlockEntity> {

	public FluidShellBlock(Properties properties) {
		super(properties);
	}
	
	@Override public Class<FluidShellBlockEntity> getTileEntityClass() { return FluidShellBlockEntity.class; }
	@Override public BlockEntityType<? extends FluidShellBlockEntity> getTileEntityType() { return CBCBlockEntities.FLUID_SHELL.get(); }
	
	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		FluidShellProjectile projectile = CBCEntityTypes.FLUID_SHELL.create(level);
		projectile.setFuze(getFuze(blockEntity));
		projectile.setFluid(getFluid(blockEntity));
		return projectile;
	}
	
	public static FluidStack getFluid(BlockEntity blockEntity) {
		return blockEntity instanceof FluidShellBlockEntity shell ? shell.tank.getFluidInTank(0).copy() : FluidStack.EMPTY;
	}

}
