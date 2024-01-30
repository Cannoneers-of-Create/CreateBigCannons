package rbasamoyai.createbigcannons.munitions;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import rbasamoyai.createbigcannons.munitions.config.MunitionProperties;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

import javax.annotation.Nullable;

public class CannonDamageSource extends IndirectEntityDamageSource {

	public CannonDamageSource(String id, Entity entity, @Nullable Entity owner) {
		super(id, entity, owner);
		MunitionProperties properties = MunitionPropertiesHandler.getProperties(entity);
		if (properties instanceof BaseProjectileProperties bpp && bpp.ignoresEntityArmor()) this.bypassArmor();
	}

}
