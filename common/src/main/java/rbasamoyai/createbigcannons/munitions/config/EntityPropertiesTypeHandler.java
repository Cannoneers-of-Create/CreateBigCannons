package rbasamoyai.createbigcannons.munitions.config;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public abstract class EntityPropertiesTypeHandler<PROPERTIES> extends PropertiesTypeHandler<EntityType<?>, PROPERTIES> {

	@Nonnull public final PROPERTIES getPropertiesOf(Entity entity) { return this.getPropertiesOf(entity.getType()); }

}
