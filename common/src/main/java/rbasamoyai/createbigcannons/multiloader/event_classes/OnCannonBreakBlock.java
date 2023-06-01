package rbasamoyai.createbigcannons.multiloader.event_classes;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface OnCannonBreakBlock {
	BlockPos getAffectedBlockPos();
	BlockState getAffectedBlockState();
	BlockEntity getBlockEntity();
	ResourceLocation getResourceLocation();
}
