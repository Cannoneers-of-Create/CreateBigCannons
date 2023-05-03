package rbasamoyai.createbigcannons.fabric.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class OnCannonBreakBlockImpl implements OnCannonBreakBlock {
	private BlockPos blockPos;
	private BlockState blockState;
	private ResourceLocation resourceLocation;
	public OnCannonBreakBlockImpl(BlockPos blockPos, BlockState blockState, ResourceLocation resourceLocation) {
		this.blockPos = blockPos;
		this.blockState = blockState;
		this.resourceLocation = resourceLocation;
	}
	@Override
	public BlockPos getAffectedBlockPos() {
		return this.blockPos;
	}

	@Override
	public BlockState getAffectedBlockState() {
		return this.blockState;
	}
	@Override
	public ResourceLocation getResourceLocation() {
		return this.resourceLocation;
	}
}
