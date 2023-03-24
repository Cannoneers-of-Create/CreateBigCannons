package rbasamoyai.createbigcannons.cannons;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

public class AutocannonBreechSlotView extends SnapshotParticipant<ItemStack> implements StorageView<ItemVariant> {

	protected AutocannonBreechInterface inventory;
	protected boolean isInput;

	public AutocannonBreechSlotView(AutocannonBreechInterface inventory, boolean isInput) {
		this.inventory = inventory;
		this.isInput = isInput;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return this.isInput ? 0 : this.inventory.extract(resource, maxAmount, transaction);
	}

	@Override public boolean isResourceBlank() { return this.getResource().isBlank(); }
	@Override public ItemVariant getResource() { return ItemVariant.of(this.inventory.getStack(this.isInput)); }
	@Override public long getAmount() { return 0; }

	@Override
	public long getCapacity() {
		return 1;
	}

	@Override protected ItemStack createSnapshot() {
		return this.inventory.getStack(this.isInput).copy();
	}

	@Override protected void readSnapshot(ItemStack snapshot) { this.inventory.restoreViewSnapshot(this.isInput, snapshot); }

}
