package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BigCartridgeBlockEntity extends SyncedBlockEntity {

	private int storedPower;

	public BigCartridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("Power", this.storedPower);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.storedPower = tag.getInt("Power");
	}

	public void setPower(int power) { this.storedPower = power; }
	public int getPower() { return this.storedPower; }

}
