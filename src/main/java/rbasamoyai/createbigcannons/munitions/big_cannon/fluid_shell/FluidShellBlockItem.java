package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

public class FluidShellBlockItem extends FuzedProjectileBlockItem {

	public FluidShellBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag tag = stack.getOrCreateTag();
        EndFluidStack efstack = EndFluidStack.readTag(stack.getOrCreateTag().getCompound("FluidContent"));
        IndexPlatform.addFluidShellComponents(efstack.fluid(), efstack.amount(), efstack.data(), tooltip);
	}

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @org.jetbrains.annotations.Nullable Player player, ItemStack stack, BlockState state) {
        if (level.getBlockEntity(pos) instanceof AbstractFluidShellBlockEntity be)
            be.readFluidDataFromFluidShellItem(stack, level.registryAccess());
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }
}
