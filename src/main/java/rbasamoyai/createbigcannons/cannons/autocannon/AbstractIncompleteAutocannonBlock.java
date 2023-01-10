package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.foundation.block.ITE;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlockEntity;

public abstract class AbstractIncompleteAutocannonBlock extends AutocannonBaseBlock implements ITE<IncompleteAutocannonBlockEntity> {

	private final NonNullSupplier<? extends Block> resultBlockSup;
	private Block resultBlock;

	protected AbstractIncompleteAutocannonBlock(Properties properties, AutocannonMaterial material, NonNullSupplier<? extends Block> resultBlock) {
		super(properties, material);
		this.resultBlockSup = resultBlock;
	}

	@Override public boolean isComplete(BlockState state) { return false; }
	@Override public boolean isBreechMechanism(BlockState state) { return false; }

	@Override public Class<IncompleteAutocannonBlockEntity> getTileEntityClass() { return IncompleteAutocannonBlockEntity.class; }
	@Override public BlockEntityType<? extends IncompleteAutocannonBlockEntity> getTileEntityType() { return CBCBlockEntities.INCOMPLETE_AUTOCANNON.get(); }

	protected Block getResultBlock() {
		if (this.resultBlock == null) this.resultBlock = this.resultBlockSup.get();
		return this.resultBlock.delegate.get();
	}

}
