package rbasamoyai.createbigcannons.munitions.mortarstone;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.config.CBCConfigs;

import java.util.List;

public class MortarStoneItem extends BlockItem {

    public MortarStoneItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ItemDescription.Palette palette = AllSections.of(stack).getTooltipPalette();
        if (Screen.hasShiftDown()) {
            String key = this.getDescriptionId() + ".tooltip.maximumCharges";
            tooltip.add(new TranslatableComponent(key).withStyle(ChatFormatting.GRAY));
            String value = String.format("%.2f", CBCConfigs.SERVER.munitions.maxMortarStoneCharges.get());
            tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", value), palette.color, palette.hColor, 1));
        }
    }
}
