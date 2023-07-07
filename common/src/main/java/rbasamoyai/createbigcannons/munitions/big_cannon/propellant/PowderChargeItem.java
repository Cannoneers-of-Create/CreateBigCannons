package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;

import java.util.List;

public class PowderChargeItem extends BlockItem {

	public PowderChargeItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
	}

}
