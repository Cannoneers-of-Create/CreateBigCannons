package rbasamoyai.createbigcannons.cannons;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.CannonMaterial.FailureMode;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonTooltip {

	public static <T extends Block & CannonBlock> void appendText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		
		String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
		if (holdDesc.length >= 2) {
			Component keyShift = Lang.translateDirect("tooltip.keyShift");
			MutableComponent tabBuilder = new TextComponent("");
			tabBuilder.append(new TextComponent(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
			tabBuilder.append(keyShift.plainCopy().withStyle(desc ? ChatFormatting.WHITE : ChatFormatting.GRAY));
			tabBuilder.append(new TextComponent(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
			tooltip.add(tabBuilder);
		}
		
		ItemDescription.Palette palette = AllSections.of(stack).getTooltipPalette();
		if (desc) {
			CannonMaterial material = block.getCannonMaterial();
			Minecraft mc = Minecraft.getInstance();
			boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
			String rootKey = "block." + CreateBigCannons.MOD_ID + ".cannon.tooltip";
			tooltip.add(new TextComponent(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));
			
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".strength")).withStyle(ChatFormatting.GRAY));
			int strength = material.maxSafeCharges();
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".strength.goggles", strength), palette.color, palette.hColor, 2));
			} else {
				tooltip.add(getNoGogglesMeter(strength == 0 ? 0 : strength / 2 + 1, false, true));
			}
			
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".squibRatio")).withStyle(ChatFormatting.GRAY));
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".squibRatio.goggles", material.squibRatioNum(), material.squibRatioDem()), palette.color, palette.hColor, 2));
			} else {
				double squibRatio = material.squibRatio();
				tooltip.add(getNoGogglesMeter(squibRatio < 1d ? 0 : Mth.ceil(material.squibRatio() * 5d / 3d), false, true));
			}
			
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
			float weightImpact = material.weight();
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.color, palette.hColor, 2));
			} else {
				tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : (int)(weightImpact * 0.5f), true, true));
			}
			
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".onFailure")).withStyle(ChatFormatting.GRAY));
			String failKey = material.failureMode() == FailureMode.RUPTURE ? ".onFailure.rupture" : ".onFailure.fragment";
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + failKey), palette.color, palette.hColor, 1));
			
			if (ForgeRegistries.BLOCKS.tags().getTag(CBCTags.BlockCBC.WEAK_CANNON_END).contains(block) && CBCConfigs.SERVER.cannons.weakBreechStrength.get() != -1) {
				int weakCharges = CBCConfigs.SERVER.cannons.weakBreechStrength.get();
				tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".weakCannonEnd")).withStyle(ChatFormatting.GRAY));
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".weakCannonEnd.desc", weakCharges), palette.color, palette.hColor, 2));
			}
		}
	}
	
	private static Component getNoGogglesMeter(int outOfFive, boolean invertColor, boolean canBeInvalid) {
		int value = invertColor ? 5 - outOfFive : outOfFive;
		ChatFormatting color = switch (value) {
			case 0, 1 -> ChatFormatting.RED;
			case 2, 3 -> ChatFormatting.GOLD;
			case 4, 5 -> ChatFormatting.YELLOW;
			default -> canBeInvalid ? ChatFormatting.DARK_GRAY : value < 0 ? ChatFormatting.RED : ChatFormatting.YELLOW;
		};
		return new TextComponent(" " + ItemDescription.makeProgressBar(5, outOfFive)).withStyle(color);
	}
	
}
