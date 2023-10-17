package rbasamoyai.createbigcannons.munitions.big_cannon.he_shell;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class HEShellBlock extends SimpleShellBlock {

	public HEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		HEShellProjectile projectile = CBCEntityTypes.HE_SHELL.get().create(level);
		projectile.setFuze(getFuze(projectileBlocks));
		return projectile;
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.HE_SHELL.get(); }

}
