package rbasamoyai.createbigcannons.munitions.blank;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

public class BlankBlock extends ProjectileBlock {
	
	public BlankBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		return CBCEntityTypes.BLANK.get().create(level);
	}
	
}
