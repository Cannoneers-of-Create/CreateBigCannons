package rbasamoyai.createbigcannons.compat.copycats;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import rbasamoyai.createbigcannons.block_armor_properties.mimicking_blocks.MimickingBlockArmorUnit;

public class BinaryCopycatArmorProperties extends MultiStateCopycatArmorProperties {

    private final BooleanProperty[] propertiesToCount;

    protected BinaryCopycatArmorProperties(MimickingBlockArmorUnit defaultTotalMultiplier,
                                           Map<Integer, MimickingBlockArmorUnit> totalMultiplierByCount,
                                           BooleanProperty... propertiesToCount) {
        super(defaultTotalMultiplier, totalMultiplierByCount);
        this.propertiesToCount = propertiesToCount;
    }

    @Override
    protected int getElementCount(Level level, BlockState state, BlockPos pos) {
        int count = 0;
        for (BooleanProperty property : this.propertiesToCount) {
            if (state.getOptionalValue(property).orElse(false))
                ++count;
        }
        return Math.max(count, 1);
    }

    public static Factory<BinaryCopycatArmorProperties> of(BooleanProperty... properties) {
        return (defaultTotalMultiplier, totalMultiplierByCount) -> new BinaryCopycatArmorProperties(defaultTotalMultiplier, totalMultiplierByCount, properties);
    }

}
