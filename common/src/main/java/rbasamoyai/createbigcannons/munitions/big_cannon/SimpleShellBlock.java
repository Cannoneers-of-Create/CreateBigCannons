package rbasamoyai.createbigcannons.munitions.big_cannon;

import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public abstract class SimpleShellBlock<T extends FuzedBigCannonProjectileProperties, E extends FuzedBigCannonProjectile<T>>
	extends FuzedProjectileBlock<FuzedBlockEntity, T, E> {

	protected SimpleShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<FuzedBlockEntity> getBlockEntityClass() {
		return FuzedBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends FuzedBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.FUZED_BLOCK.get();
	}

}
