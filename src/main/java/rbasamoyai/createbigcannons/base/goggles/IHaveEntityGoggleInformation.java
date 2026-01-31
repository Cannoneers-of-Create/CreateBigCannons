package rbasamoyai.createbigcannons.base.goggles;

import java.util.List;

import net.minecraft.network.chat.Component;

public interface IHaveEntityGoggleInformation {

	default boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		return false;
	}

}
