package rbasamoyai.createbigcannons.munitions.fragment_burst;

import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record ProjectileBurstProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage, int lifetime) {
}
