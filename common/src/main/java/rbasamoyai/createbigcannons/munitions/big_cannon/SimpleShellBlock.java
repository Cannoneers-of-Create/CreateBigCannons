package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public abstract class SimpleShellBlock extends FuzedProjectileBlock<FuzedBlockEntity> {

	protected SimpleShellBlock(Properties properties) {
		super(properties);
	}
	
	@Override public Class<FuzedBlockEntity> getTileEntityClass() { return FuzedBlockEntity.class; }
	@Override public BlockEntityType<? extends FuzedBlockEntity> getTileEntityType() { return CBCBlockEntities.FUZED_BLOCK.get(); }

}
