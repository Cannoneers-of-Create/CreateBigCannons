package rbasamoyai.createbigcannons.fabric.munitions.autocannon;

import java.util.Iterator;

import javax.annotation.Nonnull;

import io.github.fabricators_of_create.porting_lib.transfer.StorageViewArrayIterator;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;

public class AutocannonAmmoContainerInterface extends SnapshotParticipant<AutocannonAmmoContainerInterface.ContainerSnapshot> implements Storage<ItemVariant> {

	private final AutocannonAmmoContainerBlockEntity be;
	private final AutocannonAmmoContainerSlotView[] views;

    public AutocannonAmmoContainerInterface(AutocannonAmmoContainerBlockEntity be) {
        this.be = be;
        this.views = new AutocannonAmmoContainerSlotView[2];
		this.views[0] = new AutocannonAmmoContainerSlotView(this, false);
		this.views[1] = new AutocannonAmmoContainerSlotView(this, true);
    }

    @Override
	public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (maxAmount < 1 || this.be.isCreativeContainer()) return 0;
		ItemStack stack = resource.toStack();
		AutocannonAmmoType ammoType = this.be.getAmmoType();
		if (!ammoType.isValidMunition(stack)) return 0;
		long total = 0;

		this.updateSnapshots(transaction);
		ItemStack mainAmmoStack = this.be.getMainAmmoStack();
		if (mainAmmoStack.isEmpty() || resource.matches(mainAmmoStack)) {
			int capacity = this.be.getMainAmmoCapacity() - Math.max(mainAmmoStack.getCount(), 0);
			if (capacity > 0) {
				int add = Math.min((int) maxAmount, capacity);
				this.be.setMainAmmoDirect(ItemHandlerHelper.copyStackWithSize(stack, mainAmmoStack.getCount() + add));
				maxAmount -= add;
				total += add;
			}
		}
		this.updateSnapshots(transaction);
		ItemStack tracerStack = this.be.getTracerStack();
		if (maxAmount > 0 && (tracerStack.isEmpty() || resource.matches(tracerStack))) {
			int capacity = this.be.getTracerAmmoCapacity() - Math.max(tracerStack.getCount(), 0);
			if (capacity > 0) {
				int add = Math.min((int) maxAmount, capacity);
				this.be.setTracersDirect(ItemHandlerHelper.copyStackWithSize(stack, tracerStack.getCount() + add));
				stack.shrink(add);
				total += add;
			}
		}
		return total;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (maxAmount < 1) return 0;
		long total = 0;

		boolean isCreative = this.be.isCreativeContainer();

		this.updateSnapshots(transaction);
		ItemStack mainStack = this.be.getMainAmmoStack();
		if (resource.matches(mainStack)) {
			if (isCreative) return maxAmount;

			long add = Math.min(mainStack.getCount(), maxAmount);
			if (add > 0) {
				mainStack.split((int) add);
				if (mainStack.isEmpty()) this.be.setMainAmmoDirect(ItemStack.EMPTY);
				total += add;
			}
		}
		this.updateSnapshots(transaction);
		ItemStack tracerStack = this.be.getTracerStack();
		if (resource.matches(tracerStack)) {
			if (isCreative) return maxAmount;

			long add = Math.min(tracerStack.getCount(), maxAmount - total);
			if (add > 0) {
				tracerStack.split((int) add);
				if (tracerStack.isEmpty()) this.be.setTracersDirect(ItemStack.EMPTY);
				total += add;
			}
		}
		return total;
	}

	@Nonnull
	public ItemStack getStack(boolean isTracers) {
		return isTracers ? this.be.getTracerStack() : this.be.getMainAmmoStack();
	}

	@Override
	public Iterator<StorageView<ItemVariant>> iterator() {
		return new StorageViewArrayIterator<>(this.views);
	}

	@Override protected ContainerSnapshot createSnapshot() { return new ContainerSnapshot(this); }
	@Override protected void readSnapshot(ContainerSnapshot snapshot) { snapshot.apply(this); }

	public int getCapacityForSlot(boolean isTracer) {
		return isTracer ? this.be.getTracerAmmoCapacity() : this.be.getMainAmmoCapacity();
	}

	public void restoreViewSnapshot(boolean isTracer, ItemStack snapshot) {
		if (isTracer) {
			this.be.setTracersDirect(snapshot);
		} else {
			this.be.setMainAmmoDirect(snapshot);
		}
	}

	public static class ContainerSnapshot {
		private final ItemStack ammo;
		private final ItemStack tracers;

		public ContainerSnapshot(AutocannonAmmoContainerInterface inventory) {
			this.ammo = inventory.be.getMainAmmoStack().copy();
			this.tracers = inventory.be.getTracerStack().copy();
		}

		public void apply(AutocannonAmmoContainerInterface inventory) {
			inventory.be.setMainAmmoDirect(this.ammo);
			inventory.be.setTracersDirect(this.tracers);
		}
	}

}
