package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
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
		CompoundTag beTag = tag.getCompound("BlockEntityTag");
		CompoundTag fluidTag = beTag.getCompound("FluidContent");
		ResourceLocation fluidId = ResourceLocation.tryParse(fluidTag.getString("FluidName"));
		Fluid fluid = fluidId == null ? Fluids.EMPTY : Registry.FLUID.get(fluidId);
		long count = fluidTag.getLong("Amount");
		IndexPlatform.addFluidShellComponents(fluid, count, tooltip);
	}

}
