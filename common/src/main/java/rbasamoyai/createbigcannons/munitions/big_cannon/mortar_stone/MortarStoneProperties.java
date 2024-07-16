package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public record MortarStoneProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
								   BigCannonProjectilePropertiesComponent bigCannonProperties, ExplosionPropertiesComponent explosion,
								   float maxCharges) {
}
