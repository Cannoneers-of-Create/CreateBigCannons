package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

public interface IAutocannonBlockEntity {

    ItemCannonBehavior cannonBehavior();

    default void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {}

}
