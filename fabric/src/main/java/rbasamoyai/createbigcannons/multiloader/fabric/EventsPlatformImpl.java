package rbasamoyai.createbigcannons.multiloader.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.fabric.events.OnCannonBreakBlockEvent;
import rbasamoyai.createbigcannons.fabric.events.OnCannonBreakBlockImpl;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class EventsPlatformImpl {
	public static void postOnCannonBreakBlockEvent(OnCannonBreakBlock event) {
		OnCannonBreakBlockEvent.EVENT.invoker().OnCannonBreakBlockImpl(event);
	}

	public static OnCannonBreakBlock createOnCannonBreakBlockEvent(BlockPos blockPos, BlockState blockState, ResourceLocation resourceLocation) {
		return new OnCannonBreakBlockImpl(blockPos, blockState, resourceLocation);
	}
}