package rbasamoyai.createbigcannons.fabric.cannon_control;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

public class CannonCarriageEntity extends AbstractCannonCarriageEntity {

	public CannonCarriageEntity(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected ItemStack insertItemIntoCannon(ItemStack item, AbstractMountedCannonContraption cannon) {
		if (!(cannon instanceof ItemTransferable it)) return item;
		Storage<ItemVariant> storage = it.getItemStorage(null);
		if (storage == null) return item;
		ItemStack copy = item.copy();
		copy.shrink((int) TransferUtil.insert(storage, ItemVariant.of(item), 1));
		return copy;
	}

}
