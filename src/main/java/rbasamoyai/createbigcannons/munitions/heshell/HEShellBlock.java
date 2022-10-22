package rbasamoyai.createbigcannons.munitions.heshell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.SimpleShellBlock;

public class HEShellBlock extends SimpleShellBlock {

	public HEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		HEShellProjectile projectile = CBCEntityTypes.HE_SHELL.get().create(level);
		projectile.setFuze(getFuze(blockEntity));
		return projectile;
	}
	
}
