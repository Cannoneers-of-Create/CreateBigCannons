package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class PowderChargeItem extends BlockItem {

	public PowderChargeItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
		CBCTooltip.appendPropellantStressText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.POWDER_CHARGE.get());
		if (stack.getOrCreateTag().getBoolean("Damp"))
			tooltipComponents.add(Components.translatable("block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.damp").withStyle(ChatFormatting.BLUE));
	}

}
