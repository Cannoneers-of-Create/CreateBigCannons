package rbasamoyai.createbigcannons.cannons.autocannon;

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
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

public class AutocannonBlockItem<T extends Block & AutocannonBlock> extends BlockItem {

    private final T autocannonBlock;

    public AutocannonBlockItem(T block, Properties properties) {
        super(block, properties);
        this.autocannonBlock = block;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        CBCTooltip.appendTextAutocannon(stack, ctx, tooltip, flag, this.autocannonBlock);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
		Player player = context.getPlayer();
		AutocannonMaterial material = this.autocannonBlock.getAutocannonMaterial();
		if (player != null && (material.properties().connectsInSurvival() || player.isCreative()))
			AutocannonBlock.onPlace(context.getLevel(), context.getClickedPos());
        return result;
    }

}
