package rbasamoyai.createbigcannons.base.goggles;

import java.util.List;

import net.minecraft.network.chat.Component;

public interface IHaveEntityHoverInformation {

	default boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return false;
	}

}
