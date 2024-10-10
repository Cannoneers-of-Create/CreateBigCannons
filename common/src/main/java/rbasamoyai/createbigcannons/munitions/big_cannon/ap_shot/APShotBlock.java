package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;

public class APShotBlock extends InertProjectileBlock {

	public APShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHOT.get();
	}

}
