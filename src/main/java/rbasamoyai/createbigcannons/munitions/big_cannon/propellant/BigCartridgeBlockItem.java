package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;

public class BigCartridgeBlockItem extends BlockItem {

	private final BigCartridgeBlock cartridgeBlock;

	public BigCartridgeBlockItem(BigCartridgeBlock block, Properties properties) {
		super(block, properties);
		this.cartridgeBlock = block;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
		CBCTooltip.appendBigCartridgePropellantPowerText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
		CBCTooltip.appendMuzzleVelocityText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
		CBCTooltip.appendPropellantStressText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
		if (stack.getOrDefault(CBCDataComponents.DAMP, false))
			tooltipComponents.add(Component.translatable("block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.damp").withStyle(ChatFormatting.BLUE));
	}

    public int getMaximumPowerLevels() {
		return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this.cartridgeBlock).maxPowerLevels();
	}

	public static int getPower(ItemStack stack) {
		return stack.has(CBCDataComponents.POWER) ? stack.get(CBCDataComponents.POWER) : 0;
	}

	public static ItemStack getWithPower(int power) {
		ItemStack stack = CBCBlocks.BIG_CARTRIDGE.asStack();
        stack.set(CBCDataComponents.POWER, power);
		return stack;
	}

}
