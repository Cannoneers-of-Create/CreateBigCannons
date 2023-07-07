package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public abstract class AbstractIncompleteAutocannonBlock extends AutocannonBaseBlock implements IBE<IncompleteAutocannonBlockEntity> {

	protected AbstractIncompleteAutocannonBlock(Properties properties, AutocannonMaterial material) {
		super(properties, material);
	}

	@Override
	public boolean isComplete(BlockState state) {
		return false;
	}

	@Override
	public boolean isBreechMechanism(BlockState state) {
		return false;
	}

	@Override
	public Class<IncompleteAutocannonBlockEntity> getBlockEntityClass() {
		return IncompleteAutocannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends IncompleteAutocannonBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.INCOMPLETE_AUTOCANNON.get();
	}

}
