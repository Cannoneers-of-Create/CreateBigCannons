package rbasamoyai.createbigcannons.base.goggles;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface IHaveEntityGoggleInformation {

	default boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return false;
	}

}
