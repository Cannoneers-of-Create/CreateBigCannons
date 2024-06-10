package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;

public class SolidShotBlock extends InertProjectileBlock {

	public SolidShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SHOT.get();
	}

}
