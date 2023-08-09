package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;

public class ShrapnelShellBlock extends SimpleShellBlock {

	public ShrapnelShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		ShrapnelShellProjectile projectile = CBCEntityTypes.SHRAPNEL_SHELL.create(level);
		projectile.setFuze(getFuze(blockEntity));
		return projectile;
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.SHRAPNEL_SHELL.get(); }

}
