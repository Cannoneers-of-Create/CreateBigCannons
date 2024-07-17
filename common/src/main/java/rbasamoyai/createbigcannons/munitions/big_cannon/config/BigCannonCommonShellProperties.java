package rbasamoyai.createbigcannons.munitions.big_cannon.config;

import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public record BigCannonCommonShellProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
											 BigCannonProjectilePropertiesComponent bigCannonProperties,
											 BigCannonFuzePropertiesComponent fuze, ExplosionPropertiesComponent explosion) {
}
