package rbasamoyai.createbigcannons.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.multiloader.event_classes.OnCannonBreakBlock;

public class EventsPlatform {

	@ExpectPlatform
	public static void postOnCannonBreakBlockEvent(OnCannonBreakBlock event) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static OnCannonBreakBlock createOnCannonBreakBlockEvent(BlockPos blockPos, BlockState blockState) {
		throw new AssertionError();
	}
}
