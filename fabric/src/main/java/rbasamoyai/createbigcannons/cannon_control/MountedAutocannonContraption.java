package rbasamoyai.createbigcannons.cannon_control;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannons.AutocannonBreechBlockEntity;

public class MountedAutocannonContraption extends AbstractMountedAutocannonContraption implements ItemTransferable {

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
		return this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech
				? breech.createItemHandler()
				: null;
	}

}
