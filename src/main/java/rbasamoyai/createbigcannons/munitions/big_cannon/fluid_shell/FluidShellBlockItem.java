package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class FluidShellBlockItem extends FuzedProjectileBlockItem {

	public FluidShellBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        CustomData data = stack.getOrDefault(CBCDataComponents.FLUID_CONTENT, CustomData.EMPTY);
		CompoundTag fluidTag = data.copyTag().getCompound("Fluid");
		ResourceLocation fluidId = ResourceLocation.tryParse(fluidTag.getString("id"));
		Fluid fluid = fluidId == null ? Fluids.EMPTY : CBCRegistryUtils.getFluid(fluidId);
		long count = fluidTag.getLong("amount");
		IndexPlatform.addFluidShellComponents(fluid, count, tooltip);
	}

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof AbstractFluidShellBlockEntity be)
            be.readFluidDataFromFluidShellItem(context.getItemInHand(), context.getLevel().registryAccess());
        return result;
    }
}
