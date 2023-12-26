package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public interface PropertiesMunitionItem<T extends MunitionProperties> {

	@Nullable
	default T getProperties() {
		return (T) MunitionPropertiesHandler.getProperties((Item) this);
	}

}
