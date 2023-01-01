package rbasamoyai.createbigcannons.cannonmount.carriage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCTooltip;

import javax.annotation.Nullable;
import java.util.List;

public class CannonCarriageBlockItem extends BlockItem {

	public CannonCarriageBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (level != null) CBCTooltip.appendCannonCarriageText(stack, level, tooltip, flag, this);
	}

}
