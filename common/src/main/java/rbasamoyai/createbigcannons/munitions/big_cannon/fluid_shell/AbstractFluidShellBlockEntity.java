package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;

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

	public abstract boolean tryEmptyItemIntoTE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, Direction side);
	public abstract boolean tryFillItemFromTE(Level world, Player player, InteractionHand handIn, ItemStack heldItem, Direction side);

	public static int getFluidShellCapacity() {
		FluidShellProperties properties = CBCMunitionPropertiesHandlers.FLUID_SHELL.getPropertiesOf(CBCEntityTypes.FLUID_SHELL.get());
		return IndexPlatform.convertFluid(properties.fluidShellCapacity());
	}

}
