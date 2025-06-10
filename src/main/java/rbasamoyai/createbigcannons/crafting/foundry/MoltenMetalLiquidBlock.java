package rbasamoyai.createbigcannons.crafting.foundry;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCLiquidBlock;

public class MoltenMetalLiquidBlock extends CBCLiquidBlock {

	public MoltenMetalLiquidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

}
