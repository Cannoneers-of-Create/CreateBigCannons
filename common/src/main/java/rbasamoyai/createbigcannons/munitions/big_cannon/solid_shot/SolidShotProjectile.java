package rbasamoyai.createbigcannons.munitions.big_cannon.solid_shot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;

public class SolidShotProjectile extends AbstractBigCannonProjectile<BigCannonProjectileProperties> {

	public SolidShotProjectile(EntityType<? extends SolidShotProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.SOLID_SHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Override
	protected boolean canDeflect(BlockHitResult result) {
		return super.canDeflect(result) && isDeflector(this.level.getBlockState(result.getBlockPos()));
	}

}
