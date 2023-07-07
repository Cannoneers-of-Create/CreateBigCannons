package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class MortarStoneBlock extends ProjectileBlock {

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity blockEntity) {
        return CBCEntityTypes.MORTAR_STONE.create(level);
    }

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.MORTAR_STONE.get(); }

}
