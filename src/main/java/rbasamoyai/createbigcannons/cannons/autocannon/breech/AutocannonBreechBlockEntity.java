package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public class AutocannonBreechBlockEntity extends AbstractAutocannonBreechBlockEntity {

	private IItemHandler inventory;


	public AutocannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public IItemHandler createItemHandler() {
		return this.inventory == null ? this.inventory = new AutocannonBreechInterface(this) : this.inventory;
	}

	@Override
	public void requestModelDataUpdate() {
		super.requestModelDataUpdate();
        if (!this.remove)
            EnvExecute.executeOnClient(() -> () -> VisualizationHelper.queueUpdate(this));
	}

	@Override
	protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(tag, registries, clientPacket);
		if (clientPacket && !this.isVirtual()) this.requestModelDataUpdate();
	}

}
