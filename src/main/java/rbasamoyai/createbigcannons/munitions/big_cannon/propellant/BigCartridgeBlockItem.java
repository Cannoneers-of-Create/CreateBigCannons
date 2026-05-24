package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;

public class BigCartridgeBlockItem extends BlockItem {

	private final BigCartridgeBlock cartridgeBlock;

	public BigCartridgeBlockItem(BigCartridgeBlock block, Properties properties) {
		super(block, properties);
		this.cartridgeBlock = block;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		CBCTooltip.appendBigCartridgePropellantPowerText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
		CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
		CBCTooltip.appendPropellantStressText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
		if (stack.getOrCreateTag().getBoolean("Damp"))
			tooltipComponents.add(Component.translatable("block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.damp").withStyle(ChatFormatting.BLUE));
	}

	public int getMaximumPowerLevels() {
		return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this.cartridgeBlock).maxPowerLevels();
	}

	public static int getPower(ItemStack stack) {
		return stack.getOrCreateTag().getInt("Power");
	}

	public static ItemStack getWithPower(int power) {
		ItemStack stack = CBCBlocks.BIG_CARTRIDGE.asStack();
		stack.getOrCreateTag().putInt("Power", power);
		return stack;
	}

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        if (level.getBlockEntity(pos) instanceof BigCartridgeBlockEntity cartridgeBE)
            cartridgeBE.setPower(getPower(stack));
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

}
