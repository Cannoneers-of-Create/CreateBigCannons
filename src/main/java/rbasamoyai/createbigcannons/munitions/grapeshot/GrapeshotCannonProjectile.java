package rbasamoyai.createbigcannons.munitions.grapeshot;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.DisintegratingCannonProjectile;
import rbasamoyai.createbigcannons.munitions.shrapnel.Shrapnel;

public class GrapeshotCannonProjectile extends DisintegratingCannonProjectile {

	public GrapeshotCannonProjectile(EntityType<? extends GrapeshotCannonProjectile> type, Level level) {
		super(type, level);
	}
	
	@Override
	protected void disintegrate() {
		int count = CBCConfigs.SERVER.munitions.grapeshotCount.get();
		float spread = CBCConfigs.SERVER.munitions.grapeshotSpread.getF();
		float damage = CBCConfigs.SERVER.munitions.grapeshotDamage.getF();
		Shrapnel.spawnShrapnelBurst(this.level, CBCEntityTypes.GRAPESHOT.get(), this.position(), this.getDeltaMovement(), count, spread, damage);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.BAG_OF_GRAPESHOT.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
