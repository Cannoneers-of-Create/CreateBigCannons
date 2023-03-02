package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shell;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;

public class APShellProjectile extends FuzedBigCannonProjectile {

	public APShellProjectile(EntityType<? extends APShellProjectile> type, Level level) {
		super(type, level);
		this.setProjectileMass(24);
	}
	
	@Override
	protected void detonate() {
		this.level.explode(null, this.getX(), this.getY(), this.getZ(), CBCConfigs.SERVER.munitions.apShellPower.getF(), CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		this.discard();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCBlocks.AP_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

}
