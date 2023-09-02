package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.DisintegratingBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.Shrapnel;
import rbasamoyai.createbigcannons.munitions.config.ShrapnelProperties;

public class GrapeshotCannonProjectile extends DisintegratingBigCannonProjectile {

	public GrapeshotCannonProjectile(EntityType<? extends GrapeshotCannonProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void disintegrate() {
		ShrapnelProperties properties = this.getProperties().shrapnel();
		Shrapnel.spawnShrapnelBurst(this.level(), CBCEntityTypes.GRAPESHOT.get(), this.position(), this.getDeltaMovement(),
				properties.count(), properties.spread(), (float) properties.damage());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BAG_OF_GRAPESHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
