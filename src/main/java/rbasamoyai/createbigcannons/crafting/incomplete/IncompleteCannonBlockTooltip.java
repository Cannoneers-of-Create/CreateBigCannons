package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.List;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class IncompleteCannonBlockTooltip {

	public static void addToTooltip(List<Component> tooltip, boolean isPlayerSneaking, IncompleteCannonBlock incomplete, BlockState state) {
		Lang.builder("block")
				.translate(CreateBigCannons.MOD_ID + ".incomplete_block.tooltip.requiredParts")
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip);
		
		List<ItemLike> required = incomplete.requiredItems();
		int currentState = incomplete.progress(state);
		for (int i = 0; i < required.size(); ++i) {
			LangBuilder lb = Lang.builder();
			lb.text(i == currentState ? "> " : "")
			.add(new TranslatableComponent(required.get(i).asItem().getDescriptionId()))
			.style(i == currentState ? ChatFormatting.WHITE : ChatFormatting.DARK_GRAY);
			if (i < currentState) lb.style(ChatFormatting.STRIKETHROUGH);
			lb.forGoggles(tooltip, 1);
		}
	}
	
}
