package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot.SolidShotProjectile;

public class APShotProjectile extends SolidShotProjectile {

	public APShotProjectile(EntityType<? extends APShotProjectile> type, Level level) {
		super(type, level);
		this.setBreakthroughPower((byte) 5);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.AP_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
