package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public record FluidShellProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
								   BigCannonProjectilePropertiesComponent bigCannonProperties,
								   BigCannonFuzePropertiesComponent fuze, ExplosionPropertiesComponent explosion,
								   int fluidShellCapacity, int mBPerFluidBlob, int mBPerAoeRadius, float fluidBlobSpread) {
}
