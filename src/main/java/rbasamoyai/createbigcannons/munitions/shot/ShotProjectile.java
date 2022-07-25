package rbasamoyai.createbigcannons.munitions.shot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class ShotProjectile extends AbstractCannonProjectile {

	public ShotProjectile(EntityType<? extends ShotProjectile> type, Level level) {
		super(type, level);
		this.setBreakthroughPower((byte) 5);
	}
	
	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SOLID_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
