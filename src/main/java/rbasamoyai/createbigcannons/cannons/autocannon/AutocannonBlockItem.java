package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.cannons.CannonTooltip;

import java.util.List;

public class AutocannonBlockItem<T extends Block & AutocannonBlock> extends BlockItem {

    private final T autocannonBlock;

    public AutocannonBlockItem(T block, Item.Properties properties) {
        super(block, properties);
        this.autocannonBlock = block;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CannonTooltip.appendTextAutocannon(stack, level, tooltip, flag, this.autocannonBlock);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        AutocannonBlock.onPlace(context.getLevel(), context.getClickedPos());
        return result;
    }

}
