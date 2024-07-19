package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;
import xfacthd.framedblocks.common.blockentity.special.FramedCollapsibleCopycatBlockEntity;

public class FramedCollapsibleCopycatBlockArmorProperties extends SingleFramedBlockArmorProperties {

	public FramedCollapsibleCopycatBlockArmorProperties(MimickingBlockArmorUnit defaultUnit, Map<BlockState, MimickingBlockArmorUnit> unitsByState) {
		super(defaultUnit, unitsByState);
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return super.toughness(level, state, pos, recurse) * this.getCollapsibleMultiplier(level, state, pos);
	}

	public double getCollapsibleMultiplier(Level level, BlockState state, BlockPos pos) {
		if (!((level.getBlockEntity(pos)) instanceof FramedCollapsibleCopycatBlockEntity fccbe)
			|| fccbe.getCamo().getState().getDestroySpeed(level, pos) == -1) return 1;
		int x = 16;
		int y = 16;
		int z = 16;
		for (Direction dir : Iterate.directions) {
			int offset = fccbe.getFaceOffset(dir);
			switch (dir.getAxis()) {
                case X -> x -= offset;
                case Y -> y -= offset;
                case Z -> z -= offset;
            }
		}
		double scaledVolume = (double)(x * y * z) / 4096d;
		return Math.ceil(scaledVolume * 64d) / 64d;
	}

}
