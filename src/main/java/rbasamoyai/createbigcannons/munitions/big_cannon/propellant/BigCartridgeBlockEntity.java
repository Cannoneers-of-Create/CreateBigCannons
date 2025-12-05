package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCItems;

public class BigCartridgeBlockEntity extends SyncedBlockEntity implements SpecialBlockEntityItemRequirement {

	private int storedPower;

	public BigCartridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("Power", this.storedPower);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.storedPower = tag.getInt("Power");
	}

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        if (this.storedPower <= 0)
            return ItemRequirement.NONE;
        List<ItemStack> powders = new ArrayList<>();
        int power = this.storedPower;
        int maxSz = 64;
        while (power > 0) {
            int stackSz = Math.min(power, maxSz);
            powders.add(CBCItems.NITROPOWDER.asStack(stackSz));
            power -= stackSz;
        }
        // This doesn't seem to actually work when printing. Shoot an Issue/PR to Create to fix SchematicPrinter#getCurrentRequirement --ritchie
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, powders);
    }

    public void setPower(int power) { this.storedPower = power; }
	public int getPower() { return this.storedPower; }

}
