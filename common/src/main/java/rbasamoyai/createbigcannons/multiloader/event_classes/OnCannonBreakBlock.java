package rbasamoyai.createbigcannons.multiloader.event_classes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface OnCannonBreakBlock {
	BlockPos getAffectedBlockPos();
	BlockState getAffectedBlockState();
}
