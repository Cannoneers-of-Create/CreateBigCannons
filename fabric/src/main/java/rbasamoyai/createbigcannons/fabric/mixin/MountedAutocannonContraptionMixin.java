package rbasamoyai.createbigcannons.fabric.mixin;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.fabric.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.fabric.mixin_interface.GetItemStorage;

import javax.annotation.Nullable;

@Mixin(MountedAutocannonContraption.class)
public abstract class MountedAutocannonContraptionMixin extends AbstractMountedCannonContraption implements ItemCannon, GetItemStorage {

	@Override
	public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
		Storage<ItemVariant> storage = this.getItemStorage();
		if (storage == null) return stack;

		if (simulate) {
			try (Transaction t = Transaction.openOuter()) {
				ItemStack copy = stack.copy();
				copy.shrink((int) TransferUtil.insert(storage, ItemVariant.of(stack), 1));
				t.abort();
				return copy;
			}
		} else {
			ItemStack copy = stack.copy();
			copy.shrink((int) TransferUtil.insert(storage, ItemVariant.of(stack), 1));
			return copy;
		}

	}

	@Override
	public ItemStack extractItemFromCannon(boolean simulate) {
		Storage<ItemVariant> storage = this.getItemStorage();
		if (storage == null) return ItemStack.EMPTY;

		if (simulate) {
			try (Transaction t = Transaction.openOuter()) {
				ItemStack result = TransferUtil.extractAnyItem(storage, 1);
				t.abort();
				return result;
			}
		} else {
			return TransferUtil.extractAnyItem(storage, 1);
		}
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage() {
		return this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech ? breech.createItemHandler() : null;
	}

}
