package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannonsClient;

import javax.annotation.Nullable;
import java.util.List;

public class CannonCarriageTooltip {

    public static void appendText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, BlockItem block) {
        ItemDescription.Palette palette = AllSections.of(stack).getTooltipPalette();
        if (Screen.hasShiftDown()) {
            String key = block.getDescriptionId() + ".tooltip";



            String fire = I18n.get(CreateBigCannonsClient.FIRE_CONTROLLED_CANNON.getTranslatedKeyMessage().getString());
            tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", fire), ChatFormatting.GRAY, ChatFormatting.WHITE));
            tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".fireCannon"), palette.color, palette.hColor, 1));

            String pitchMode = I18n.get(CreateBigCannonsClient.PITCH_MODE.getTranslatedKeyMessage().getString());
            tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", pitchMode), ChatFormatting.GRAY, ChatFormatting.WHITE));
            tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".pitchMode"), palette.color, palette.hColor, 1));
        }
    }

}
