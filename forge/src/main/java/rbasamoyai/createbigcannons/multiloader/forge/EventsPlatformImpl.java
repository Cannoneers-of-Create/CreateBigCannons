package rbasamoyai.createbigcannons.multiloader.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import rbasamoyai.createbigcannons.forge.events.OnCannonBreakBlockImpl;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class EventsPlatformImpl {

	// Cast is necessary to share the underlying interface.
	public static void postOnCannonBreakBlockEvent(OnCannonBreakBlock event) {
		MinecraftForge.EVENT_BUS.post((OnCannonBreakBlockImpl) event);
	}

	public static OnCannonBreakBlock createOnCannonBreakBlockEvent(BlockPos blockPos, BlockState blockState, ResourceLocation resourceLocation) {
		return new OnCannonBreakBlockImpl(blockPos, blockState, resourceLocation);
	}
}