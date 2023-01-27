package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.base.CBCTooltip;

import java.util.List;

public class MortarStoneItem extends BlockItem {

    public MortarStoneItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CBCTooltip.appendMortarStoneText(stack, level, tooltip, flag);
    }

}
