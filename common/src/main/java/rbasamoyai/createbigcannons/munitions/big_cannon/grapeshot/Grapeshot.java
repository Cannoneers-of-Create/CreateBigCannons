package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;

public class Grapeshot extends Shrapnel {

	public Grapeshot(EntityType<? extends Grapeshot> type, Level level) {
		super(type, level);
	}

	@Override
	protected DamageSource getDamageSource() {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.GRAPESHOT), this);
	}

}
