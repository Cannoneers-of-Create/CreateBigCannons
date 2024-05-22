package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.CommonShellBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class APShellBlock extends SimpleShellBlock<CommonShellBigCannonProjectileProperties, APShellProjectile> {

	public APShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends APShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.AP_SHELL.get();
	}

}
