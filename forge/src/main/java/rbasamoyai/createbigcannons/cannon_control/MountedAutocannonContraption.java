package rbasamoyai.createbigcannons.cannon_control;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannons.AutocannonBreechBlockEntity;

public class MountedAutocannonContraption extends AbstractMountedAutocannonContraption implements ItemCannon {

	@Override
	public LazyOptional<IItemHandler> getItemOptional() {
		return this.presentTileEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech
				? LazyOptional.of(breech::createItemHandler)
				: LazyOptional.empty();
	}

}
