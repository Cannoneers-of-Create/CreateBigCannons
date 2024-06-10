package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class SmokeShellProjectile extends FuzedBigCannonProjectile {

	public SmokeShellProjectile(EntityType<? extends SmokeShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate() {
		SmokeShellProperties properties = this.getAllProperties();
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), 2, Explosion.BlockInteraction.NONE);
		SmokeEmitterEntity smoke = CBCEntityTypes.SMOKE_EMITTER.create(this.level);
		smoke.setPos(this.position());
		smoke.setDuration(properties.smokeDuration());
		smoke.setSize(properties.smokeScale());
		this.level.addFreshEntity(smoke);
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SMOKE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected SmokeShellProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.SMOKE_SHELL.getPropertiesOf(this);
	}

}
