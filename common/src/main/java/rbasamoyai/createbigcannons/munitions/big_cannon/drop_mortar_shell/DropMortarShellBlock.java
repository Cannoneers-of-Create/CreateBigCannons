package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarMunition;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class DropMortarShellBlock extends SimpleShellBlock<DropMortarShellProperties, DropMortarShellProjectile> implements DropMortarMunition {

	public DropMortarShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile<? extends DropMortarProjectileProperties> getProjectile(Level level, ItemStack stack) {
		DropMortarShellProjectile projectile = CBCEntityTypes.DROP_MORTAR_SHELL.get().create(level);
		projectile.setFuze(DropMortarMunition.getFuze(stack));
		projectile.setTracer(DropMortarMunition.getTracer(stack));
		return projectile;
	}

	@Override
	public EntityType<? extends DropMortarShellProjectile> getAssociatedEntityType() {
		return CBCEntityTypes.DROP_MORTAR_SHELL.get();
	}

}
