package rbasamoyai.createbigcannons.base;

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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.CreateBigCannonsClientHooks;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial.FailureMode;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.manualloading.RamRodItem;
import rbasamoyai.createbigcannons.manualloading.WormItem;

import javax.annotation.Nullable;
import java.util.List;

public class CBCTooltip {

	public static <T extends Block & BigCannonBlock> void appendCannonBlockText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, T block) {
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
		
		ItemDescription.Palette palette = getPalette(level, stack);
		if (desc) {
			BigCannonMaterial material = block.getCannonMaterial();
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

			if (block.defaultBlockState().is(CBCTags.BlockCBC.WEAK_CANNON_END) && CBCConfigs.SERVER.cannons.weakBreechStrength.get() != -1) {
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

	public static ItemDescription.Palette getPalette(Level level, ItemStack stack) {
		if (level == null) return AllSections.UNASSIGNED.getTooltipPalette();
		AllSections section = AllSections.of(stack);
		return section == null ? AllSections.UNASSIGNED.getTooltipPalette() : section.getTooltipPalette();
	}

	public static <T extends Block & AutocannonBlock> void appendTextAutocannon(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, T block) {
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

		ItemDescription.Palette palette = getPalette(level, stack);
		if (desc) {
			AutocannonMaterial material = block.getAutocannonMaterial();
			Minecraft mc = Minecraft.getInstance();
			boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
			String rootKey = "block." + CreateBigCannons.MOD_ID + ".autocannon.tooltip";
			tooltip.add(new TextComponent(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

			int maxLength = material.maxLength();
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".maxLength")).withStyle(ChatFormatting.GRAY));
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".maxLength.goggles", maxLength + 1), palette.color, palette.hColor, 2));
			} else {
				tooltip.add(getNoGogglesMeter(maxLength == 0 ? 0 : (maxLength - 1) / 2 + 1, false, true));
			}

			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
			float weightImpact = material.weight();
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.color, palette.hColor, 2));
			} else {
				tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : Mth.ceil(weightImpact), true, true));
			}
		}
	}

	public static void appendMortarStoneText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		ItemDescription.Palette palette = getPalette(level, stack);
		if (Screen.hasShiftDown()) {
			String key = stack.getDescriptionId() + ".tooltip.maximumCharges";
			tooltip.add(new TranslatableComponent(key).withStyle(ChatFormatting.GRAY));
			String value = String.format("%.2f", CBCConfigs.SERVER.munitions.maxMortarStoneCharges.get());
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", value), palette.color, palette.hColor, 1));
		}
	}

	public static void appendRamRodText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		ItemDescription.Palette palette = getPalette(level, stack);
		if (Screen.hasShiftDown()) {
			String keyBase = stack.getDescriptionId() + ".tooltip.";

			String key = keyBase + "pushStrength";
			tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", RamRodItem.getPushStrength()), palette.color, palette.hColor, 1));

			String key1 = keyBase + "reach";
			tooltip.add(new TextComponent(I18n.get(key1)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".value", RamRodItem.getReach()), palette.color, palette.hColor, 1));

			String key2 = keyBase + "deployerCanUse";
			tooltip.add(new TextComponent(I18n.get(key2)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2 + (RamRodItem.deployersCanUse() ? ".yes" : ".no")), palette.color, palette.hColor, 1));
		}
	}

	public static void appendWormText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		ItemDescription.Palette palette = getPalette(level, stack);
		if (Screen.hasShiftDown()) {
			String keyBase = stack.getDescriptionId() + ".tooltip.";

			String key = keyBase + "reach";
			tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", WormItem.getReach()), palette.color, palette.hColor, 1));

			String key1 = keyBase + "deployerCanUse";
			tooltip.add(new TextComponent(I18n.get(key1)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + (WormItem.deployersCanUse() ? ".yes" : ".no")), palette.color, palette.hColor, 1));
		}
	}

	public static void appendImpactFuzeText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, float detChance) {
		ItemDescription.Palette palette = getPalette(level, stack);
		if (Screen.hasShiftDown()) {
			String key = stack.getDescriptionId() + ".tooltip.chance";
			tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", (int)(detChance * 100.0f)), palette.color, palette.hColor, 1));
		}
	}

	public static void appendCannonCarriageText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, BlockItem block) {
		ItemDescription.Palette palette = getPalette(level, stack);
		if (Screen.hasShiftDown()) {
			String key = block.getDescriptionId() + ".tooltip";

			String fire = I18n.get(CreateBigCannonsClientHooks.FIRE_CONTROLLED_CANNON.getTranslatedKeyMessage().getString());
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", fire), ChatFormatting.GRAY, ChatFormatting.WHITE));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".fireCannon"), palette.color, palette.hColor, 1));

			String pitchMode = I18n.get(CreateBigCannonsClientHooks.PITCH_MODE.getTranslatedKeyMessage().getString());
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", pitchMode), ChatFormatting.GRAY, ChatFormatting.WHITE));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".pitchMode"), palette.color, palette.hColor, 1));
		}
	}

}
