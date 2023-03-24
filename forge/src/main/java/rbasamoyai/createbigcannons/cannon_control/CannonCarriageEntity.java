package rbasamoyai.createbigcannons.cannon_control;

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
		if (!(cannon instanceof ItemCannon ic)) return item;
		return ic.getItemOptional().map(h -> {
			return h.insertItem(1, item, false);
		}).orElse(item);
	}


}
