package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class PowderChargeItem extends BlockItem {

	public PowderChargeItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
		CBCTooltip.appendMuzzleVelocityText(stack, ctx, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
		CBCTooltip.appendPropellantStressText(stack, ctx, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
		if (stack.getOrDefault(CBCDataComponents.DAMP, false))
			tooltipComponents.add(Component.translatable("block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.damp").withStyle(ChatFormatting.BLUE));
	}

}
