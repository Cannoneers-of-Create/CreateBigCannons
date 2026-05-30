package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import static rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity.getFluidShellCapacity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

public class FluidShellBlockItem extends FuzedProjectileBlockItem {

	public FluidShellBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        CustomData data = stack.getOrDefault(CBCDataComponents.FLUID_CONTENT, CustomData.EMPTY);
        HolderLookup.Provider registries = ctx.registries();
        if (!data.isEmpty() && registries != null) { // jank alert
            FluidTank tank = new FluidTank(getFluidShellCapacity()).readFromNBT(registries, data.copyTag()); // It's cheaper than recreating the entire BE.
            FluidStack fstack = tank.getFluid();
            IndexPlatform.addFluidShellComponents(fstack.getFluid(), fstack.getComponentsPatch(), fstack.getAmount(), tooltip);
        } else {
            IndexPlatform.addFluidShellComponents(Fluids.EMPTY, DataComponentPatch.EMPTY, 0, tooltip);
        }
	}

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        return result;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        if (level.getBlockEntity(pos) instanceof AbstractFluidShellBlockEntity be)
            be.readFluidDataFromFluidShellItem(stack, level.registryAccess());
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

}
