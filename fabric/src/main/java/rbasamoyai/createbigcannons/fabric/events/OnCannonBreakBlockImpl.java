package rbasamoyai.createbigcannons.fabric.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class OnCannonBreakBlockImpl implements OnCannonBreakBlock {
	private BlockPos blockPos;
	private BlockState blockState;
	public OnCannonBreakBlockImpl(BlockPos blockPos, BlockState blockState) {
		this.blockPos = blockPos;
		this.blockState = blockState;
	}
	@Override
	public BlockPos getAffectedBlockPos() {
		return this.blockPos;
	}

	@Override
	public BlockState getAffectedBlockState() {
		return this.blockState;
	}
}
