package rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks;

import java.util.Map;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatBlockArmorProperties extends AbstractMimickingBlockArmorProperties {

	public CopycatBlockArmorProperties(MimickingBlockArmorUnit defaultUnit, Map<BlockState, MimickingBlockArmorUnit> unitsByState) {
		super(defaultUnit, unitsByState);
	}

	@Override protected BlockState getCopiedState(Level level, BlockState state, BlockPos pos) { return CopycatBlock.getMaterial(level, pos); }

	@Override
	protected boolean isEmptyState(Level level, BlockState copiedState, BlockState state, BlockPos pos) {
		return super.isEmptyState(level, copiedState, state, pos) || AllBlocks.COPYCAT_BASE.has(copiedState);
	}

}
