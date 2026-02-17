package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof AbstractFluidShellBlockEntity be)
            be.readFluidDataFromFluidShellItem(context.getItemInHand(), context.getLevel().registryAccess());
        return result;
    }

}
