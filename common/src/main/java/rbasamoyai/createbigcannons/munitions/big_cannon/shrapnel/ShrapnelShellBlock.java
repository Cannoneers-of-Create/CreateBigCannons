package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class ShrapnelShellBlock extends SimpleShellBlock<ShrapnelShellProjectile> {

	public ShrapnelShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.SHRAPNEL_SHELL.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ShrapnelShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.SHRAPNEL_SHELL.get();
	}

}
