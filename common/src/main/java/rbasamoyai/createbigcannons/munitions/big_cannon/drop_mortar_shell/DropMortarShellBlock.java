package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import java.util.List;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarMunition;
import rbasamoyai.createbigcannons.munitions.big_cannon.DropMortarProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMunitionEntity;

public class DropMortarShellBlock extends SimpleShellBlock<DropMortarShellProperties> implements DropMortarMunition {

	public DropMortarShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractBigCannonProjectile<?> getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		DropMortarShellProjectile projectile = CBCEntityTypes.DROP_MORTAR_SHELL.get().create(level);
		projectile.setFuze(getFuze(projectileBlocks));
		return projectile;
	}

	@Override
	public AbstractBigCannonProjectile<? extends DropMortarProjectileProperties> getProjectile(Level level, ItemStack stack) {
		DropMortarShellProjectile projectile = CBCEntityTypes.DROP_MORTAR_SHELL.get().create(level);
		projectile.setFuze(DropMortarMunition.getFuze(stack));
		return projectile;
	}

	@Override
	public EntityType<? extends PropertiesMunitionEntity<? extends DropMortarShellProperties>> getAssociatedEntityType() {
		return CBCEntityTypes.DROP_MORTAR_SHELL.get();
	}

}
