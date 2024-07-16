package rbasamoyai.createbigcannons.munitions;

import javax.annotation.Nullable;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

public class CannonDamageSource extends IndirectEntityDamageSource {

	public CannonDamageSource(String id, Entity entity, @Nullable Entity owner, boolean bypassArmor) {
		super(id, entity, owner);
		if (bypassArmor)
			this.bypassArmor();
	}

}
