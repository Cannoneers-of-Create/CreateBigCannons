package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.InertBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class SolidShotProjectile extends AbstractBigCannonProjectile {

	public SolidShotProjectile(EntityType<? extends SolidShotProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SOLID_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected InertBigCannonProjectileProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.INERT_BIG_CANNON_PROJECTILE.getPropertiesOf(this);
	}

}
