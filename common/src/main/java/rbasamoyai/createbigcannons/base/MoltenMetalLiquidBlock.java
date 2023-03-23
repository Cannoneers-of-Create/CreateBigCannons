package rbasamoyai.createbigcannons.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class MoltenMetalLiquidBlock extends LiquidBlock {

	public MoltenMetalLiquidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// TODO: burn entity
		super.entityInside(state, level, pos, entity);
	}

}
