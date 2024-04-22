package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public class MortarStoneBlock extends ProjectileBlock<MortarStoneProperties> {

    public MortarStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractBigCannonProjectile<?> getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
        return CBCEntityTypes.MORTAR_STONE.create(level);
    }

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends MortarStoneProperties>> getAssociatedEntityType() {
		return CBCEntityTypes.MORTAR_STONE.get();
	}

}
