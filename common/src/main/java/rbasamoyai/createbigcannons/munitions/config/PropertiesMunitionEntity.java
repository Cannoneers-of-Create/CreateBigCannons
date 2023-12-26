package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface PropertiesMunitionEntity<T extends MunitionProperties> {

	@Nullable
	default T getProperties() {
		return (T) MunitionPropertiesHandler.getProperties((Entity) this);
	}

}
