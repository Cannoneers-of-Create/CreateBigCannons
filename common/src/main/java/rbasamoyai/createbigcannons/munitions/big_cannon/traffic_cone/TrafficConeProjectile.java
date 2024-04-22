package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;

public class TrafficConeProjectile extends AbstractBigCannonProjectile<BigCannonProjectileProperties> {

	public TrafficConeProjectile(EntityType<? extends TrafficConeProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.TRAFFIC_CONE.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.TRAFFIC_CONE), this);
	}

}
