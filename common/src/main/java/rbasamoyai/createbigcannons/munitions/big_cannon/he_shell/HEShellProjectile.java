package rbasamoyai.createbigcannons.munitions.big_cannon.he_shell;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.CommonShellBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;

public class HEShellProjectile extends FuzedBigCannonProjectile<CommonShellBigCannonProjectileProperties> {

	public HEShellProjectile(EntityType<? extends HEShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate() {
		CommonShellBigCannonProjectileProperties properties = this.getProperties();
		this.level().explode(null, this.indirectArtilleryFire(), null, this.getX(), this.getY(),
			this.getZ(), properties == null ? 0 : properties.explosivePower(), false,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.HE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
