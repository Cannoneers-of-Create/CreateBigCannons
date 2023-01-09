package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.IncompleteItemCannonBehavior;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

public class IncompleteAutocannonBlockEntity extends AutocannonBlockEntity {
	public IncompleteAutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override protected ItemCannonBehavior makeBehavior() { return new IncompleteItemCannonBehavior(this); }

}
