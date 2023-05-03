package rbasamoyai.createbigcannons.munitions.big_cannon.traffic_cone;

import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.CBCDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;

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
		CBCDamageSource src = new CBCDamageSource(CreateBigCannons.MOD_ID + ".traffic_cone");
		if (this.getProperties().ignoresEntityArmor()) src.bypassArmor();
		return src;
	}

}
