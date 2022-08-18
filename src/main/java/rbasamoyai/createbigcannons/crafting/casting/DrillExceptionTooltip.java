package rbasamoyai.createbigcannons.crafting.casting;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity.FailureReason;

public class DrillExceptionTooltip {
	
	public static void addToTooltip(FailureReason reason, List<Component> tooltip, boolean isSneaking) {
		Lang.builder("exception")
			.translate(CreateBigCannons.MOD_ID + ".cannon_drill.encounteredProblem")
			.style(ChatFormatting.GOLD)
			.forGoggles(tooltip);
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get("exception." + CreateBigCannons.MOD_ID + ".cannon_drill." + reason.getSerializedName()), ChatFormatting.GRAY, ChatFormatting.WHITE, 5));
	}
	
}
