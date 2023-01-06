package rbasamoyai.createbigcannons.cannons;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCTooltip;

public class CannonBlockItem<T extends Block & CannonBlock> extends BlockItem {

	private final T cannonBlock;
	
	public CannonBlockItem(T block, Properties properties) {
		super(block, properties);
		this.cannonBlock = block;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (level != null) CBCTooltip.appendCannonBlockText(stack, level, tooltip, flag, this.cannonBlock);
	}
	
	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		CannonBlock.onPlace(context.getLevel(), context.getClickedPos());
		return result;
	}

}
