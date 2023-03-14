package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.ArrayList;
import java.util.List;

public interface IAutocannonBlockEntity extends ICannonBlockEntity<ItemCannonBehavior> {

    default void tickFromContraption(Level level, AbstractPitchOrientedContraptionEntity poce, BlockPos localPos) {}

    default List<ItemStack> getDrops() {
        List<ItemStack> list = new ArrayList<>();
        list.add(this.cannonBehavior().getItem());
        return list;
    }

}
