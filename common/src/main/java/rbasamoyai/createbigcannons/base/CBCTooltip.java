package rbasamoyai.createbigcannons.base;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties;
import rbasamoyai.createbigcannons.index.CBCBigCannonMaterials;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.manual_loading.RamRodItem;
import rbasamoyai.createbigcannons.manual_loading.WormItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStoneProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

public class CBCTooltip {
	private static Style primary = TooltipHelper.Palette.GRAY_AND_WHITE.primary();
	private static Style highlight = TooltipHelper.Palette.GRAY_AND_WHITE.highlight();

	private static void addHoldShift(boolean desc, List<Component> tooltip) {
		String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
		if (holdDesc.length < 2) return;
		Component keyShift = Lang.translateDirect("tooltip.keyShift");
		MutableComponent tabBuilder = new TextComponent("");
		tabBuilder.append(new TextComponent(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
		tabBuilder.append(keyShift.plainCopy().withStyle(desc ? ChatFormatting.WHITE : ChatFormatting.GRAY));
		tabBuilder.append(new TextComponent(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
		tooltip.add(tabBuilder);
	}

	private static Component getNoGogglesMeter(int outOfFive, boolean invertColor, boolean canBeInvalid) {
		int value = invertColor ? 5 - outOfFive : outOfFive;
		ChatFormatting color = switch (value) {
			case 0, 1 -> ChatFormatting.RED;
			case 2, 3 -> ChatFormatting.GOLD;
			case 4, 5 -> ChatFormatting.YELLOW;
			default -> canBeInvalid ? ChatFormatting.DARK_GRAY : value < 0 ? ChatFormatting.RED : ChatFormatting.YELLOW;
		};
		return new TextComponent(" " + TooltipHelper.makeProgressBar(5, outOfFive)).withStyle(color);
	}

	public static TooltipHelper.Palette getPalette(Level level, ItemStack stack) {
		return TooltipHelper.Palette.STANDARD_CREATE;
	}

	public static <T extends Block & BigCannonBlock> void appendCannonBlockText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) return;

		TooltipHelper.Palette palette = getPalette(level, stack);
		BigCannonMaterialProperties material = block.getCannonMaterial().properties();
		Minecraft mc = Minecraft.getInstance();
		boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
		String rootKey = "block." + CreateBigCannons.MOD_ID + ".cannon.tooltip";
		tooltip.add(new TextComponent(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".strength")).withStyle(ChatFormatting.GRAY));
		float rawStrength = material.maxSafePropellantStress();

		if (hasGoggles) {
			String strength = rawStrength > 1000 ? I18n.get(rootKey + ".strength.unlimited") : String.format("%.2f", rawStrength);
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".strength.goggles", strength), palette.primary(), palette.highlight(), 2));
		} else {
			float nethersteelStrength = CBCBigCannonMaterials.NETHERSTEEL.properties().maxSafePropellantStress();
			int strength = Mth.ceil(Math.min(rawStrength / nethersteelStrength * 5, 5));
			tooltip.add(getNoGogglesMeter(strength, false, true));
		}

		double minVelPerBarrel = material.minimumVelocityPerBarrel();
		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".cannonJamming")).withStyle(ChatFormatting.GRAY));
		if (hasGoggles) {
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".cannonJamming.goggles", String.format("%.2f", minVelPerBarrel * 20)), palette.primary(), palette.highlight(), 2));
		} else {
			tooltip.add(getNoGogglesMeter(minVelPerBarrel < 1d ? 0 : Mth.ceil(minVelPerBarrel * 5d / 6d), false, true));
		}

		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
		float weightImpact = material.weight();
		if (hasGoggles) {
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.primary(), palette.highlight(), 2));
		} else {
			tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : (int) (weightImpact * 0.5f), true, true));
		}

		if (block.getDefaultOpeningType() == BigCannonEnd.CLOSED) {
			tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".breechStrength")).withStyle(ChatFormatting.GRAY));
			float breechStrength = BigCannonBreechStrengthHandler.getStrength(block, material.maxSafePropellantStress());
			if (hasGoggles) {
				tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".breechStrength.desc", breechStrength), palette.primary(), palette.highlight(), 2));
			} else {
				float nethersteelStrength = CBCBigCannonMaterials.NETHERSTEEL.properties().maxSafePropellantStress();
				int strength = Mth.ceil(Math.min(breechStrength / nethersteelStrength * 5, 5));
				tooltip.add(getNoGogglesMeter(strength, false, true));
			}
		}

		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".onFailure")).withStyle(ChatFormatting.GRAY));
		String failKey = material.failureMode() == BigCannonMaterialProperties.FailureMode.RUPTURE ? ".onFailure.rupture" : ".onFailure.fragment";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + failKey), palette.primary(), palette.highlight(), 2));
	}

	public static <T extends Block & AutocannonBlock> void appendTextAutocannon(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, T block) {
		boolean desc = Screen.hasShiftDown();
		addHoldShift(desc, tooltip);
		if (!desc) return;

		TooltipHelper.Palette palette = getPalette(level, stack);
		AutocannonMaterial material = block.getAutocannonMaterial();
		Minecraft mc = Minecraft.getInstance();
		boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
		String rootKey = "block." + CreateBigCannons.MOD_ID + ".autocannon.tooltip";
		tooltip.add(new TextComponent(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

		int maxLength = material.properties().maxBarrelLength();
		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".maxBarrelLength")).withStyle(ChatFormatting.GRAY));
		if (hasGoggles) {
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".maxBarrelLength.goggles", maxLength + 1), palette.primary(), palette.highlight(), 2));
		} else {
			tooltip.add(getNoGogglesMeter(maxLength == 0 ? 0 : (maxLength - 1) / 2 + 1, false, true));
		}

		tooltip.add(new TextComponent(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
		float weightImpact = material.properties().weight();
		if (hasGoggles) {
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.primary(), palette.highlight(), 2));
		} else {
			tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : Mth.ceil(weightImpact), true, true));
		}
	}

	public static void appendMortarStoneText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = stack.getDescriptionId() + ".tooltip.maximumCharges";
		tooltip.add(new TranslatableComponent(key).withStyle(ChatFormatting.GRAY));
		MortarStoneProperties properties = (MortarStoneProperties) MunitionPropertiesHandler.getProperties(CBCEntityTypes.MORTAR_STONE.get());
		String value = String.format("%.2f", properties == null ? 2f : properties.maxCharges());
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", value), palette.primary(), palette.highlight(), 1));
	}

	public static void appendRamRodText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String keyBase = stack.getDescriptionId() + ".tooltip.";

		String key = keyBase + "pushStrength";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", RamRodItem.getPushStrength()), palette.primary(), palette.highlight(), 1));

		String key1 = keyBase + "reach";
		tooltip.add(new TextComponent(I18n.get(key1)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + ".value", RamRodItem.getReach()), palette.primary(), palette.highlight(), 1));

		String key2 = keyBase + "deployerCanUse";
		tooltip.add(new TextComponent(I18n.get(key2)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2 + (RamRodItem.deployersCanUse() ? ".yes" : ".no")), palette.primary(), palette.highlight(), 1));
	}

	public static void appendWormText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String keyBase = stack.getDescriptionId() + ".tooltip.";

		String key = keyBase + "reach";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", WormItem.getReach()), palette.primary(), palette.highlight(), 1));

		String key1 = keyBase + "deployerCanUse";
		tooltip.add(new TextComponent(I18n.get(key1)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key1 + (WormItem.deployersCanUse() ? ".yes" : ".no")), palette.primary(), palette.highlight(), 1));
	}

	public static void appendImpactFuzeText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, float detChance) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = stack.getDescriptionId() + ".tooltip.chance";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", (int) (detChance * 100.0f)), palette.primary(), palette.highlight(), 1));
	}

	public static void appendCannonCarriageText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag, BlockItem block) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = block.getDescriptionId() + ".tooltip";

		String fire = I18n.get(CBCClientCommon.FIRE_CONTROLLED_CANNON.getTranslatedKeyMessage().getString());
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", fire), primary, highlight));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".fireCannon"), palette.primary(), palette.highlight(), 1));

		String pitchMode = I18n.get(CBCClientCommon.PITCH_MODE.getTranslatedKeyMessage().getString());
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".keyPressed", pitchMode), primary, highlight));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".pitchMode"), palette.primary(), palette.highlight(), 1));
	}

	public static void appendMuzzleVelocityText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag,
												BigCannonPropellantBlock propellant) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.added_muzzle_velocity";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		String s = String.format("%+.2f", propellant.getChargePower(stack) * 20);
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", s), palette.primary(), palette.highlight(), 1));
	}

	public static void appendPropellantStressText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag,
												  BigCannonPropellantBlock propellant) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.added_stress";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		String s = String.format("%+.2f", propellant.getStressOnCannon(stack));
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", s), palette.primary(), palette.highlight(), 1));
	}

	public static void appendPropellantPowerText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag,
												 BigCannonPropellantBlock propellant) {
		if (!Screen.hasShiftDown()) return;
		TooltipHelper.Palette palette = getPalette(level, stack);
		String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.power";
		tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
		int min = BigCartridgeBlockItem.getPower(stack);
		int max = BigCartridgeBlock.getMaximumPowerLevels();
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", min, max), palette.primary(), palette.highlight(), 1));
	}

}
