package rbasamoyai.createbigcannons.forge.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class OnCannonBreakBlockImpl extends Event implements OnCannonBreakBlock {
	private BlockPos blockPos;
	private BlockState blockState;
	public OnCannonBreakBlockImpl(BlockPos blockPos, BlockState blockState) {
		this.blockPos = blockPos;
		this.blockState = blockState;
	}
	public BlockPos getAffectedBlockPos() {
			return this.blockPos;
		}
	public BlockState getAffectedBlockState() {
		return this.blockState;
	}
}
