package rbasamoyai.createbigcannons.compat.framedblocks;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.AbstractMimickingBlockArmorProperties;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;
import xfacthd.framedblocks.api.block.FramedBlockEntity;

public class SingleFramedBlockArmorProperties extends AbstractMimickingBlockArmorProperties {

	public SingleFramedBlockArmorProperties(MimickingBlockArmorUnit defaultUnit, Map<BlockState, MimickingBlockArmorUnit> unitsByState) {
		super(defaultUnit, unitsByState);
	}

	@Override
	protected BlockState getCopiedState(Level level, BlockState state, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof FramedBlockEntity fbe ? fbe.getCamo().getState() : Blocks.AIR.defaultBlockState();
	}

}
