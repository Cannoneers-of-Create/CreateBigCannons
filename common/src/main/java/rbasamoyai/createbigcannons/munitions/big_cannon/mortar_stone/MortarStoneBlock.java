package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class MortarStoneBlock extends ProjectileBlock {

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
        return CBCEntityTypes.MORTAR_STONE.create(level);
    }

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.MORTAR_STONE.get(); }

}
