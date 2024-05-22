package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class ShrapnelShellBlock extends SimpleShellBlock<ShrapnelShellProperties, ShrapnelShellProjectile> {

	public ShrapnelShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public EntityType<? extends ShrapnelShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SHRAPNEL_SHELL.get();
	}

}
