package rbasamoyai.createbigcannons.fabric.cannons;

import io.github.fabricators_of_create.porting_lib.transfer.StorageViewArrayIterator;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;

import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class AutocannonBreechInterface extends SnapshotParticipant<AutocannonBreechInterface.BreechSnapshot> implements Storage<ItemVariant> {

	private final AutocannonBreechBlockEntity breech;
	private final AutocannonBreechSlotView[] views;

	public AutocannonBreechInterface(AutocannonBreechBlockEntity breech) {
		this.breech = breech;
		this.views = new AutocannonBreechSlotView[2];
		this.views[0] = new AutocannonBreechSlotView(this, true);
		this.views[1] = new AutocannonBreechSlotView(this, false);
	}

	public boolean isItemValid(@Nonnull ItemStack stack) { return stack.getItem() instanceof AutocannonAmmoItem; }

	@Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (maxAmount < 1) return 0;
		ItemStack stack = resource.toStack();
		if (!this.isItemValid(stack) || this.breech.isInputFull()) return 0;

		Deque<ItemStack> input = this.breech.getInputBuffer();
		int maxCount = Math.min(this.breech.getQueueLimit() - input.size(), stack.getCount());
		for (int i = 0; i < maxCount; ++i) {
			updateSnapshots(transaction);
			input.add(ItemHandlerHelper.copyStackWithSize(stack, 1));
		}
		return maxCount;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		ItemStack output = this.breech.getOutputBuffer();
		if (maxAmount < 1 || output.isEmpty()) return 0;
		if (this.breech.getOutputBuffer().getItem() != resource.getItem()) return 0;

		updateSnapshots(transaction);
		this.breech.getOutputBuffer().split(1);
		return 1;
	}

	@Override
	public Iterator<StorageView<ItemVariant>> iterator(TransactionContext transaction) {
		return new StorageViewArrayIterator<>(this.views);
	}

	@Override protected BreechSnapshot createSnapshot() { return new BreechSnapshot(this); }

	@Override protected void readSnapshot(BreechSnapshot snapshot) { snapshot.apply(this); }

	public ItemStack getStack(boolean isInput) {
		if (isInput) {
			return !this.breech.isInputFull() ? ItemStack.EMPTY : this.breech.getInputBuffer().peekLast();
		}
		return this.breech.getOutputBuffer();
	}

	public void restoreViewSnapshot(boolean isInput, ItemStack stack) {
		if (isInput) {
			Deque<ItemStack> buf = this.breech.getInputBuffer();
			if (!stack.isEmpty() && !buf.isEmpty()) {
				buf.removeLast();
				buf.addLast(stack);
			}
		} else {
			this.breech.setOutputBuffer(stack);
		}
	}

	public static class BreechSnapshot {
		private final List<ItemStack> inputBuffer;
		private final ItemStack outputBuffer;

		public BreechSnapshot(AutocannonBreechInterface inventory) {
			this.inputBuffer = inventory.breech.getInputBuffer().stream().map(ItemStack::copy).toList();
			this.outputBuffer = inventory.breech.getOutputBuffer().copy();
		}

		public void apply(AutocannonBreechInterface inventory) {
			inventory.breech.getInputBuffer().clear();
			inventory.breech.getInputBuffer().addAll(this.inputBuffer);
			inventory.breech.setOutputBuffer(this.outputBuffer);
		}
	}

}
