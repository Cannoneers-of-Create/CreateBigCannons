package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class MortarStoneBlock extends ProjectileBlock<MortarStoneProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(MortarStoneBlock::new);

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
	public EntityType<? extends MortarStoneProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.MORTAR_STONE.get();
	}

}
