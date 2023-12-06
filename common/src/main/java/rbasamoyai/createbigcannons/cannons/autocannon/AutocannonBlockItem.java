package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

import java.util.List;

public class AutocannonBlockItem<T extends Block & AutocannonBlock> extends BlockItem {

    private final T autocannonBlock;

    public AutocannonBlockItem(T block, Properties properties) {
        super(block, properties);
        this.autocannonBlock = block;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CBCTooltip.appendTextAutocannon(stack, level, tooltip, flag, this.autocannonBlock);
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
