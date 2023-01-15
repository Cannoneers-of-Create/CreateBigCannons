package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

public interface IAutocannonBlockEntity extends ICannonBlockEntity<ItemCannonBehavior> {

    default void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {}

}
