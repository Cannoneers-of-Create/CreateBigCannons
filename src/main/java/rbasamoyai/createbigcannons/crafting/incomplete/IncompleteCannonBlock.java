package rbasamoyai.createbigcannons.crafting.incomplete;

import java.util.List;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

public interface IncompleteCannonBlock {

	List<ItemLike> requiredItems();
	int progress(BlockState state);
	
}
