package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public interface PropertiesMunitionBlock<T extends MunitionProperties> {

	@Nullable
	default T getProperties() {
		return (T) MunitionPropertiesHandler.getProperties((Block) this);
	}

}
