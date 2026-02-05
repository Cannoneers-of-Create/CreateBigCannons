package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class BigCartridgeBlockEntity extends SyncedBlockEntity {

	public BigCartridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setPower(int power) {
        PatchedDataComponentMap components = new PatchedDataComponentMap(this.components());
        components.set(CBCDataComponents.POWER, power);
        this.setComponents(components);
    }

	public int getPower() { return this.components().getOrDefault(CBCDataComponents.POWER, 0); }

}
