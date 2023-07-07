package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;

import java.util.List;

public class BigCartridgeBlockItem extends BlockItem {

	public BigCartridgeBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		CBCTooltip.appendPropellantPowerText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.BIG_CARTRIDGE.get());
		CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.BIG_CARTRIDGE.get());
		CBCTooltip.appendPropellantStressText(stack, level, tooltipComponents, isAdvanced, CBCBlocks.BIG_CARTRIDGE.get());
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);

		Level level = context.getLevel();
		if (!level.isClientSide && level.getBlockEntity(context.getClickedPos()) instanceof BigCartridgeBlockEntity cart) {
			cart.setPower(getPower(context.getItemInHand()));
			cart.setChanged();
		}

		return result;
	}

	public static int getPower(ItemStack stack) {
		return stack.getOrCreateTag().getInt("Power");
	}

	public static ItemStack getWithPower(int power) {
		ItemStack stack = CBCBlocks.BIG_CARTRIDGE.asStack();
		stack.getOrCreateTag().putInt("Power", power);
		return stack;
	}

}
