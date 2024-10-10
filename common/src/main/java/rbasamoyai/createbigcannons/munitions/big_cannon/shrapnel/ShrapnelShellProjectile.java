package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

public class ShrapnelShellProjectile extends FuzedBigCannonProjectile {

	public ShrapnelShellProjectile(EntityType<? extends ShrapnelShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate(Position position) {
		Vec3 oldDelta = this.getDeltaMovement();
		ShrapnelShellProperties properties = this.getAllProperties();
		ShrapnelExplosion explosion = new ShrapnelExplosion(this.level(), null, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), properties.explosion().explosivePower(),
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		CBCProjectileBurst.spawnConeBurst(this.level(), CBCEntityTypes.SHRAPNEL_BURST.get(), new Vec3(position.x(), position.y(), position.z()),
			oldDelta, properties.shrapnelBurst().burstProjectileCount(), properties.shrapnelBurst().burstSpread());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SHRAPNEL_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
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

	protected ShrapnelShellProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.SHRAPNEL_SHELL.getPropertiesOf(this);
	}

}
