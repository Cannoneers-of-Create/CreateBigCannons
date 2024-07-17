package rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
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
	protected void detonate(Position position) {
		SmokeShellProperties properties = this.getAllProperties();
		SmokeExplosion explosion = new SmokeExplosion(this.level(), null, position.x(), position.y(), position.z(), 2,
			Level.ExplosionInteraction.NONE);
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		SmokeEmitterEntity smoke = CBCEntityTypes.SMOKE_EMITTER.create(this.level());
		smoke.setPos(new Vec3(position.x(), position.y(), position.z()));
		smoke.setDuration(properties.smokeDuration());
		smoke.setSize(properties.smokeScale());
		this.level().addFreshEntity(smoke);
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
