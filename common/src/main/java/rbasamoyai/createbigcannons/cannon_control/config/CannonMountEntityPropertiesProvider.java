package rbasamoyai.createbigcannons.cannon_control.config;

import net.minecraft.world.entity.Entity;

public interface CannonMountEntityPropertiesProvider {

	float maximumElevation(Entity entity);
	float maximumDepression(Entity entity);

}
