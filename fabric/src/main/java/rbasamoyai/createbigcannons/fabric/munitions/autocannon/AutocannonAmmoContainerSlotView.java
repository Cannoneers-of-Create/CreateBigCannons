package rbasamoyai.createbigcannons.fabric.munitions.autocannon;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

public class AutocannonAmmoContainerSlotView extends SnapshotParticipant<ItemStack> implements StorageView<ItemVariant> {

	protected AutocannonAmmoContainerInterface inventory;
	protected boolean isTracer;

	public AutocannonAmmoContainerSlotView(AutocannonAmmoContainerInterface inventory, boolean isTracer) {
		this.inventory = inventory;
		this.isTracer = isTracer;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		return this.inventory.extract(resource, maxAmount, transaction);
	}

	@Override public boolean isResourceBlank() { return this.getResource().isBlank(); }
	@Override public ItemVariant getResource() { return ItemVariant.of(this.getRawStack()); }

	@Override public long getAmount() { return this.getRawStack().getCount(); }

	@Override public long getCapacity() { return this.inventory.getCapacityForSlot(this.isTracer); }

	@Override protected ItemStack createSnapshot() { return this.getRawStack().copy(); }

	public ItemStack getRawStack() { return this.inventory.getStack(this.isTracer); }

	@Override protected void readSnapshot(ItemStack snapshot) { this.inventory.restoreViewSnapshot(this.isTracer, snapshot); }

}
