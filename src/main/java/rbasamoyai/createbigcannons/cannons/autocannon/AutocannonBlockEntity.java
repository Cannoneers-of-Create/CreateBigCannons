package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class AutocannonBlockEntity extends SmartTileEntity implements IAutocannonBlockEntity {

    private ItemCannonBehavior behavior;

    public AutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        behaviours.add(this.behavior = new ItemCannonBehavior(this));
    }

    @Override public ItemCannonBehavior cannonBehavior() { return this.behavior; }

}
