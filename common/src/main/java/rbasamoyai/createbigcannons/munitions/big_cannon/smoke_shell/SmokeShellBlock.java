package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class SmokeShellBlock extends SimpleShellBlock<SmokeShellProjectile> {

	public SmokeShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.SMOKE_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends SmokeShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SMOKE_SHELL.get();
	}

}
