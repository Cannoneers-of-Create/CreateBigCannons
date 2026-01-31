package rbasamoyai.createbigcannons.cannons.big_cannons;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;

public class BigCannonBlockItem<T extends Block & BigCannonBlock> extends BlockItem {

	private final T cannonBlock;

	public BigCannonBlockItem(T block, Properties properties) {
		super(block, properties);
		this.cannonBlock = block;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        CBCTooltip.appendCannonBlockText(stack, ctx, tooltip, flag, this.cannonBlock);
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		Player player = context.getPlayer();
		BigCannonMaterial material = this.cannonBlock.getCannonMaterial();
		if (player != null && (material.properties().connectsInSurvival() || player.isCreative()))
			BigCannonBlock.onPlace(context.getLevel(), context.getClickedPos());
		return result;
	}

}
