package rbasamoyai.createbigcannons.munitions.grapeshot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.DisintegratingCannonProjectile;
import rbasamoyai.createbigcannons.munitions.shrapnel.Shrapnel;

public class GrapeshotCannonProjectile extends DisintegratingCannonProjectile {

	public GrapeshotCannonProjectile(EntityType<? extends GrapeshotCannonProjectile> type, Level level) {
		super(type, level);
	}
	
	@Override
	protected void disintegrate() {
		Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.GRAPESHOT.get(), this.position(), this.getDeltaMovement(), 12, 0.25f);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BAG_OF_GRAPESHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
