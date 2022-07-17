package rbasamoyai.createbigcannons.cannons;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;

public interface CannonBlock {

	CannonMaterial getCannonMaterial();
	Direction.Axis getAxis(BlockState state);
	Optional<Direction> getFacing(BlockState state);
	CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos);
	default boolean isConnectedToCannon(Level level, BlockState state, BlockPos pos) { return true; }
	
}
