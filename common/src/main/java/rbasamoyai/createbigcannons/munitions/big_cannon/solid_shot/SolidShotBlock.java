package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class SolidShotBlock extends ProjectileBlock {

	public SolidShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		return CBCEntityTypes.SHOT.create(level);
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.SHOT.get(); }

}
