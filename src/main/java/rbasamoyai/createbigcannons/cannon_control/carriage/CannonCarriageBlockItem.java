package rbasamoyai.createbigcannons.cannon_control.carriage;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCTooltip;

public class CannonCarriageBlockItem extends BlockItem {

	public CannonCarriageBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		if (ctx != null) CBCTooltip.appendCannonCarriageText(stack, ctx, tooltip, flag, this);
	}

}
