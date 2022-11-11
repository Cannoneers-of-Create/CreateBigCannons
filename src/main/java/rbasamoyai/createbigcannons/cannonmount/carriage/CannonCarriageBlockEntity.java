package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.IDisplayAssemblyExceptions;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CannonCarriageBlockEntity extends SyncedTileEntity implements IDisplayAssemblyExceptions {

    private AssemblyException lastException = null;

    public CannonCarriageBlockEntity(BlockEntityType<? extends CannonCarriageBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tryAssemble() {
        try {
            this.assemble();
            this.lastException = null;
        } catch (AssemblyException e) {
            this.lastException = e;
            this.sendData();
        }
    }

    protected void assemble() throws AssemblyException {

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        AssemblyException.write(tag, this.lastException);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.lastException = AssemblyException.read(tag);
    }

    @Override public AssemblyException getLastAssemblyException() { return this.lastException; }
}
