package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

public class TrafficConeProjectile extends AbstractBigCannonProjectile {

	public TrafficConeProjectile(EntityType<? extends TrafficConeProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.TRAFFIC_CONE.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Override
	protected DamageSource getEntityDamage() {
		return new CannonDamageSource(CreateBigCannons.MOD_ID + ".traffic_cone", this, null);
	}

}
