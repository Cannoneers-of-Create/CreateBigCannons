package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstParentPropertiesComponent;

public record GrapeshotBagProperties(BigCannonProjectilePropertiesComponent bigCannonProperties,
									 ProjectileBurstParentPropertiesComponent grapeshotBurst) {
}
