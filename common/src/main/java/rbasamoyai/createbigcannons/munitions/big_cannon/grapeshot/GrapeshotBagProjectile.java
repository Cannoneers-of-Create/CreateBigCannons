package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import javax.annotation.Nonnull;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.DisintegratingBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstParentPropertiesComponent;

public class GrapeshotBagProjectile extends DisintegratingBigCannonProjectile {

	public GrapeshotBagProjectile(EntityType<? extends GrapeshotBagProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void disintegrate() {
		ProjectileBurstParentPropertiesComponent properties = this.getAllProperties().grapeshotBurst();
		CBCProjectileBurst.spawnConeBurst(this.level, CBCEntityTypes.GRAPESHOT_BURST.get(), this.position(), this.getDeltaMovement(),
			properties.burstProjectileCount(), properties.burstSpread());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BAG_OF_GRAPESHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return EntityDamagePropertiesComponent.DEFAULT;
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return BallisticPropertiesComponent.DEFAULT;
	}

	protected GrapeshotBagProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.BAG_OF_GRAPESHOT.getPropertiesOf(this);
	}

}
