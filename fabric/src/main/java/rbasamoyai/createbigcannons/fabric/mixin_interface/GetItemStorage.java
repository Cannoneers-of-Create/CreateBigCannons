package rbasamoyai.createbigcannons.fabric.mixin_interface;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

import javax.annotation.Nullable;

public interface GetItemStorage {
	@Nullable Storage<ItemVariant> getItemStorage();
}
