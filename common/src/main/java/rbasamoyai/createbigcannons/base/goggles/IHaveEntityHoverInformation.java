package rbasamoyai.createbigcannons.base.goggles;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IHaveEntityHoverInformation {

	default boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return false;
	}

}
