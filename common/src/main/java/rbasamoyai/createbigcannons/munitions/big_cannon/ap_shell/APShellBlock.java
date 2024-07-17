package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class APShellBlock extends SimpleShellBlock<APShellProjectile> {

	public APShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

    @Override
	public EntityType<? extends APShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHELL.get();
	}

}
