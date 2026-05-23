package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class BigCartridgeBlockEntity extends SyncedBlockEntity implements SpecialBlockEntityItemRequirement {

	public BigCartridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setPower(int power) {
        PatchedDataComponentMap components = new PatchedDataComponentMap(this.components());
        components.set(CBCDataComponents.POWER, power);
        this.setComponents(components);
    }

	public int getPower() { return this.components().getOrDefault(CBCDataComponents.POWER, 0); }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return new ItemRequirement(new ItemRequirement.StrictNbtStackRequirement(BigCartridgeBlockItem.getWithPower(this.getPower()),
            ItemRequirement.ItemUseType.CONSUME));
    }

}
