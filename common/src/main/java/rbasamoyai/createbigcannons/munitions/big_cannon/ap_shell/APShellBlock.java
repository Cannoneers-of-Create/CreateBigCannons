package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class APShellBlock extends SimpleShellBlock {

	public APShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		APShellProjectile projectile = CBCEntityTypes.AP_SHELL.get().create(level);
		projectile.setFuze(getFuze(projectileBlocks));
		return projectile;
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.AP_SHELL.get(); }

}
