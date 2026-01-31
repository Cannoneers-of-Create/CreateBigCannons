package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.DropMortarProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record DropMortarShellProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
										BigCannonProjectilePropertiesComponent bigCannonProperties,
										BigCannonFuzePropertiesComponent fuze, DropMortarProjectilePropertiesComponent dropMortarProperties,
										float entityDamagingExplosivePower, float blockDamagingExplosivePower) implements DropMortarProjectileProperties {
}
