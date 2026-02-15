package rbasamoyai.createbigcannons.compat.copycats;

import java.util.Map;

import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class CopycatSlabArmorProperties extends MultiStateCopycatArmorProperties {

    public CopycatSlabArmorProperties(MimickingBlockArmorUnit defaultTotalMultiplier, Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount) {
        super(defaultTotalMultiplier, totalMultiplierByCount);
    }

    @Override
    protected int getElementCount(Level level, BlockState state, BlockPos pos) {
        return state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.DOUBLE ? 2 : 1;
    }

}
