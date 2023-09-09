package rbasamoyai.createbigcannons.munitions;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

public class CannonDamageSource extends DamageSource {

	private final boolean bypassArmor;

	public CannonDamageSource(Holder<DamageType> type, Entity entity) {
		super(type);
		this.bypassArmor = MunitionPropertiesHandler.getProperties(entity).ignoresEntityArmor();
	}

	@Override
	public boolean is(TagKey<DamageType> damageTypeKey) {
		// TODO: phase this out in 0.5.3, use tags instead
		if (damageTypeKey.location().equals(DamageTypeTags.BYPASSES_ARMOR.location())) return this.bypassArmor;
		return super.is(damageTypeKey);
	}

	public static Registry<DamageType> getDamageRegistry(Level level) {
		return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
	}

}
