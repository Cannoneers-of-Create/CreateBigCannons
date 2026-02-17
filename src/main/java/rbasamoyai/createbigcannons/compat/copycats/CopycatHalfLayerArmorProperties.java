package rbasamoyai.createbigcannons.compat.copycats;

import java.util.Map;

import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class CopycatHalfLayerArmorProperties extends MultiStateCopycatArmorProperties {

    public CopycatHalfLayerArmorProperties(MimickingBlockArmorUnit defaultTotalMultiplier, Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount) {
        super(defaultTotalMultiplier, totalMultiplierByCount);
    }

    @Override
    protected int getElementCount(Level level, BlockState state, BlockPos pos) {
        return Math.max(state.getValue(CopycatHalfLayerBlock.POSITIVE_LAYERS) + state.getValue(CopycatHalfLayerBlock.NEGATIVE_LAYERS), 1);
    }

}
