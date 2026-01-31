package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class APShotBlock extends InertProjectileBlock {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(APShotBlock::new);

	public APShotBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHOT.get();
	}

}
