package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

public class SolidShotProjectile extends AbstractBigCannonProjectile {

	public SolidShotProjectile(EntityType<? extends SolidShotProjectile> type, Level level) {
		super(type, level);
		this.setProjectileMass(10);
	}
	
	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SOLID_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return isDeflector(this.level.getBlockState(result.getBlockPos()));
	}

}
