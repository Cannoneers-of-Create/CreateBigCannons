package rbasamoyai.createbigcannons.block_armor_properties;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockArmorPropertiesProvider {

	double hardness(Level level, BlockState state, BlockPos pos, boolean recurse);
	double toughness(Level level, BlockState state, BlockPos pos, boolean recurse);
	List<BlockState> containedBlockStates(Level level, BlockState state, BlockPos pos, boolean recurse);

}
