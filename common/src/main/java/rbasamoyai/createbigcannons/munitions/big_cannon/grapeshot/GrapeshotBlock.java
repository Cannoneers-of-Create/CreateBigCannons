package rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

public class GrapeshotBlock extends ProjectileBlock {

	public GrapeshotBlock(Properties properties) {
		super(properties);
	}

	@Override
	public AbstractCannonProjectile getProjectile(Level level, BlockState state, BlockPos pos, BlockEntity blockEntity) {
		return CBCEntityTypes.BAG_OF_GRAPESHOT.create(level);
	}

	@Override public EntityType<?> getAssociatedEntityType() { return CBCEntityTypes.BAG_OF_GRAPESHOT.get(); }

}
