package rbasamoyai.createbigcannons.munitions.blank;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.EntityHitResult;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class BlankProjectile extends AbstractCannonProjectile {

	public BlankProjectile(EntityType<? extends BlankProjectile> type, Level level) {
		super(type, level);
		this.setBreakthroughPower((byte) 0);
	}
	
	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BLANK.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		this.discard();
	}
}
