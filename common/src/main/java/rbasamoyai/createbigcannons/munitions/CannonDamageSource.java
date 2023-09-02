package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

import javax.annotation.Nullable;

public class CannonDamageSource extends DamageSource {

	public CannonDamageSource(String id, Entity entity, @Nullable Entity owner) {
		super(id, entity, owner);
		if (MunitionPropertiesHandler.getProperties(entity).ignoresEntityArmor()) this.bypassArmor();
	}

}
