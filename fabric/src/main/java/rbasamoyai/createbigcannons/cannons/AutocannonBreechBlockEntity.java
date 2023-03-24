package rbasamoyai.createbigcannons.cannons;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractAutocannonBreechBlockEntity;

public class AutocannonBreechBlockEntity extends AbstractAutocannonBreechBlockEntity {

	private Storage<ItemVariant> inventory;

	public AutocannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public Storage<ItemVariant> createItemHandler() {
		return this.inventory == null ? this.inventory = new AutocannonBreechInterface(this) : this.inventory;
	}

}
