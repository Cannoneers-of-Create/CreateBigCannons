package rbasamoyai.createbigcannons.base.goggles;

import java.util.Arrays;
import java.util.List;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

// Copy of IDisplayAssemblyExceptions --ritchie
public interface IDisplayEntityAssemblyExceptions {

	default boolean addExceptionToTooltip(List<Component> tooltip) {
		AssemblyException e = this.getLastAssemblyException();
		if (e == null) return false;

		if (!tooltip.isEmpty()) tooltip.add(Component.empty());

		CreateLang.builder().add(CreateLang.translateDirect("gui.assembly.exception").withStyle(ChatFormatting.GOLD)).forGoggles(tooltip);

		String text = e.component.getString();
		Arrays.stream(text.split("\n"))
			.forEach(l -> TooltipHelper.cutStringTextComponent(l, FontHelper.Palette.GRAY_AND_WHITE)
				.forEach(c -> CreateLang.builder().add(c.copy()).forGoggles(tooltip)));

		return true;
	}

	AssemblyException getLastAssemblyException();

}
