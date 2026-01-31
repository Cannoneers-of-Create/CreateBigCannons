package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class ProjectileBlockItem extends BlockItem {

	public ProjectileBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.TRACER, ItemContainerContents.EMPTY);
		ItemStack tracer = items.copyOne();
		if (!tracer.isEmpty())
			tooltip.add(Component.translatable("tooltip.createbigcannons.tracer"));
	}
}
