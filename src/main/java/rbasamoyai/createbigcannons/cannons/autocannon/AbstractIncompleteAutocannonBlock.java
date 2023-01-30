package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.foundation.block.ITE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlockEntity;

public abstract class AbstractIncompleteAutocannonBlock extends AutocannonBaseBlock implements ITE<IncompleteAutocannonBlockEntity> {

	protected AbstractIncompleteAutocannonBlock(Properties properties, AutocannonMaterial material) { super(properties, material); }

	@Override public boolean isComplete(BlockState state) { return false; }
	@Override public boolean isBreechMechanism(BlockState state) { return false; }

	@Override public Class<IncompleteAutocannonBlockEntity> getTileEntityClass() { return IncompleteAutocannonBlockEntity.class; }
	@Override public BlockEntityType<? extends IncompleteAutocannonBlockEntity> getTileEntityType() { return CBCBlockEntities.INCOMPLETE_AUTOCANNON.get(); }

}
