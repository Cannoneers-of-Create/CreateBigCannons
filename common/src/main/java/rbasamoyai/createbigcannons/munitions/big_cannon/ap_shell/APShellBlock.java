package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.CommonShellBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public class APShellBlock extends SimpleShellBlock<CommonShellBigCannonProjectileProperties> {

	public APShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile<?> getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		APShellProjectile projectile = CBCEntityTypes.AP_SHELL.get().create(level);
		projectile.setFuze(getFuze(projectileBlocks));
		return projectile;
	}

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends CommonShellBigCannonProjectileProperties>> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHELL.get();
	}

}
