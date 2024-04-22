package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;
import xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleBlockEntity;

public class FramedCollapsibleBlockArmorProperties extends SingleFramedBlockArmorProperties {

	public FramedCollapsibleBlockArmorProperties(MimickingBlockArmorUnit defaultUnit, Map<BlockState, MimickingBlockArmorUnit> unitsByState) {
		super(defaultUnit, unitsByState);
	}

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return super.hardness(level, state, pos, recurse) * this.getCollapsibleMultiplier(level, state, pos);
	}

	public double getCollapsibleMultiplier(Level level, BlockState state, BlockPos pos) {
		if (!((level.getBlockEntity(pos)) instanceof FramedCollapsibleBlockEntity fcbe)
			|| fcbe.getCamo().getState().getDestroySpeed(level, pos) == -1) return 1;
		byte[] offsets = fcbe.getVertexOffsets();
		int result = 0;
		for (byte b : offsets) result += 16 - b;
		return Math.ceil(result / 16d) * 0.25d;
	}

}
