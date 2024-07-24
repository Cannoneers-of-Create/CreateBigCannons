package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.world.level.block.state.BlockState;

public interface MovesWithAutocannonRecoilSpring {

	BlockState getMovingState(BlockState original);
	BlockState getStationaryState(BlockState original);

}
