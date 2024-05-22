package rbasamoyai.createbigcannons.munitions.big_cannon.he_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.CommonShellBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class HEShellBlock extends SimpleShellBlock<CommonShellBigCannonProjectileProperties, HEShellProjectile> {

	public HEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends HEShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.HE_SHELL.get();
	}

}
