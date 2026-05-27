package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class BigCartridgeBlockEntity extends SyncedBlockEntity implements SpecialBlockEntityItemRequirement {

    protected int power;

	public BigCartridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setPower(int power) { this.power = power; }

    public int getPower() { return this.power; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Power", this.power);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = Math.max(tag.getInt("Power"), 0);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        this.power = componentInput.getOrDefault(CBCDataComponents.POWER, 0);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(CBCDataComponents.POWER, this.power);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return new ItemRequirement(new ItemRequirement.StrictNbtStackRequirement(BigCartridgeBlockItem.getWithPower(this.getPower()),
            ItemRequirement.ItemUseType.CONSUME));
    }

}
