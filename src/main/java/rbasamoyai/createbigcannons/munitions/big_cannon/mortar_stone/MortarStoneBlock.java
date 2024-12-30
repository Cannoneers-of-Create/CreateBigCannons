package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class MortarStoneBlock extends ProjectileBlock<MortarStoneProjectile> {

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
	public EntityType<? extends MortarStoneProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.MORTAR_STONE.get();
	}

}
