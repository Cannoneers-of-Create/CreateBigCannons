package rbasamoyai.createbigcannons.munitions.mortarstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

public class MortarStoneBlock extends ProjectileBlock {

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity blockEntity) {
        return CBCEntityTypes.MORTAR_STONE.create(level);
    }

}
