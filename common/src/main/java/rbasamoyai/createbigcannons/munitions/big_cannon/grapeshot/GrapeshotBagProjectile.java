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

public class GrapeshotBagProjectile extends DisintegratingBigCannonProjectile<GrapeshotBagProperties> {

	public GrapeshotBagProjectile(EntityType<? extends GrapeshotBagProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void disintegrate() {
		GrapeshotBagProperties properties = this.getProperties();
		Shrapnel.spawnShrapnelBurst(this.level(), CBCEntityTypes.GRAPESHOT.get(), this.position(), this.getDeltaMovement(),
				properties == null ? 0 : properties.grapeshotCount(),
				properties == null ? 0 : properties.grapeshotSpread(),
				properties == null ? 0 : properties.grapeshotDamage());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BAG_OF_GRAPESHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
