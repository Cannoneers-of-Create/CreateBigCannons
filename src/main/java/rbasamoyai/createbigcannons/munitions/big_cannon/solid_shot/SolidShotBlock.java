package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.InertProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class SolidShotBlock extends InertProjectileBlock {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(SolidShotBlock::new);

	public SolidShotBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

	@Override
	public EntityType<? extends AbstractBigCannonProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SHOT.get();
	}

}
