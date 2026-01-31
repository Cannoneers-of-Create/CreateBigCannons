package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
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
		CompoundTag beTag = data.copyTag().getCompound("BlockEntityTag");
		CompoundTag fluidTag = beTag.getCompound("FluidContent");
		ResourceLocation fluidId = ResourceLocation.tryParse(fluidTag.getString("FluidName"));
		Fluid fluid = fluidId == null ? Fluids.EMPTY : CBCRegistryUtils.getFluid(fluidId);
		long count = fluidTag.getLong("Amount");
		IndexPlatform.addFluidShellComponents(fluid, count, tooltip);
	}

}
