package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record SmokeShellProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
								   BigCannonProjectilePropertiesComponent bigCannonProperties,
								   BigCannonFuzePropertiesComponent fuze, float smokeScale, int smokeDuration) {
}
