package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class SmokeShellBlock extends SimpleShellBlock<SmokeShellProperties, SmokeShellProjectile> {

	public SmokeShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends SmokeShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SMOKE_SHELL.get();
	}

}
