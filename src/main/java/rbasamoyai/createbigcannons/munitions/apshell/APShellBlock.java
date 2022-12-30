package rbasamoyai.createbigcannons.munitions.apshell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.SimpleShellBlock;

public class APShellBlock extends SimpleShellBlock {

	public APShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		APShellProjectile projectile = CBCEntityTypes.AP_SHELL.get().create(level);
		projectile.setFuze(getFuze(blockEntity));
		return projectile;
	}
	
}
