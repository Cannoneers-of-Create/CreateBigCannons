package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WiredFuzeItem extends FuzeItem {

	public WiredFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onRedstoneSignal(ItemStack stack, Level level, BlockPos pos, BlockState state, int signalStrength, Direction from) {
		return signalStrength > 0;
	}

}
