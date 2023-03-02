package rbasamoyai.createbigcannons.crafting.incomplete;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public interface IncompleteWithItemsCannonBlock {

	static final IntegerProperty STAGE_2 = IntegerProperty.create("stage", 0, 1);
	
	List<ItemLike> requiredItems();
	int progress(BlockState state);
	BlockState getCompleteBlockState(BlockState state);
	
}
