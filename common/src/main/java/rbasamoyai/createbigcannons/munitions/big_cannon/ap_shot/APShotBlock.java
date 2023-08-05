package rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class APShotBlock extends ProjectileBlock {

	public APShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		return CBCEntityTypes.AP_SHOT.create(level);
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.AP_SHOT.get(); }

}
