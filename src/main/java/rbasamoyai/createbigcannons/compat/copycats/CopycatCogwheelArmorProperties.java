package rbasamoyai.createbigcannons.compat.copycats;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class CopycatCogwheelArmorProperties extends MultiStateCopycatArmorProperties {

    public CopycatCogwheelArmorProperties(MimickingBlockArmorUnit defaultTotalMultiplier, Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount) {
        super(defaultTotalMultiplier, totalMultiplierByCount);
    }

    @Override protected int getElementCount(Level level, BlockState state, BlockPos pos) { return 2; }

}
