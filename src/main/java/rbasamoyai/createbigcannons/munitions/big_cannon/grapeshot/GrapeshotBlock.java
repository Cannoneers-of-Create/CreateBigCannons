package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class GrapeshotBlock extends ProjectileBlock<GrapeshotBagProjectile> {

	public GrapeshotBlock(Properties properties) {
		super(properties);
	}

    @Override
	public EntityType<? extends GrapeshotBagProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.get();
	}

}
