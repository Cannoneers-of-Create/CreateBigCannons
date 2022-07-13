package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

public interface CannonBlock {

	CannonMaterial getCannonMaterial();
	Axis getAxis(BlockState state);
	default boolean isConnectedToCannon() { return true; }
	
}
