package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class GrapeshotBlock extends ProjectileBlock<GrapeshotBagProjectile> {

    private static final MapCodec<ProjectileBlock> CODEC = simpleCodec(GrapeshotBlock::new);

	public GrapeshotBlock(Properties properties) {
		super(properties);
	}

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
	public EntityType<? extends GrapeshotBagProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.get();
	}

}
