package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;

import java.util.List;

public abstract class AbstractFluidShellBlockEntity extends FuzedBlockEntity {
	
	protected AbstractFluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		this.addFluidToTooltip(tooltip, isPlayerSneaking);
		return true;
	}

	protected abstract void setFluidShellStack(FluidShellProjectile shell);

	protected abstract void addFluidToTooltip(List<Component> tooltip, boolean isPlayerSneaking);

	public static int getFluidShellCapacity() {
		return CBCConfigs.SERVER.munitions.fluidShellCapacity.get();
	}

}
