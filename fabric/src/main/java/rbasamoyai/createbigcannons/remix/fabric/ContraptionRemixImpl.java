package rbasamoyai.createbigcannons.remix.fabric;

import io.github.fabricators_of_create.porting_lib.util.StickinessUtil;
import net.minecraft.world.level.block.state.BlockState;

public class ContraptionRemixImpl {

	public static boolean canStickTo(BlockState state, BlockState state1) {
		return StickinessUtil.canStickTo(state, state1);
	}

}
