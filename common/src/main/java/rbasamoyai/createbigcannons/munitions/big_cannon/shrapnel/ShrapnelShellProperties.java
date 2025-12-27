package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstParentPropertiesComponent;

public record ShrapnelShellProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
									  BigCannonProjectilePropertiesComponent bigCannonProperties,
									  BigCannonFuzePropertiesComponent fuze, ExplosionPropertiesComponent explosion,
									  ProjectileBurstParentPropertiesComponent shrapnelBurst) {
}
