package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record InertBigCannonProjectileProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
												 BigCannonProjectilePropertiesComponent bigCannonProperties) {
}
