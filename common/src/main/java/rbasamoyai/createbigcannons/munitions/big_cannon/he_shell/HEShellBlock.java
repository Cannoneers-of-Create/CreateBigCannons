package rbasamoyai.createbigcannons.munitions.big_cannon.he_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class HEShellBlock extends SimpleShellBlock<HEShellProjectile> {

	public HEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends HEShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.HE_SHELL.get();
	}

}
