package rbasamoyai.createbigcannons.munitions.autocannon.config;

import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record InertAutocannonProjectileProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
												  AutocannonProjectilePropertiesComponent autocannonProperties) {
}
