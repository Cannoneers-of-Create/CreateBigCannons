package rbasamoyai.createbigcannons.neoforge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.neoforge.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.neoforge.mixin_interface.GetItemStorage;

@Mixin(MountedAutocannonContraption.class)
public abstract class MountedAutocannonContraptionMixin extends AbstractMountedCannonContraption implements ItemCannon, GetItemStorage {

	@Override
	public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
        if (this.getItemStorage() == null)
            return stack;
		return this.getItemStorage().insertItem(1, stack, simulate);
	}

	@Override
	public ItemStack extractItemFromCannon(boolean simulate) {
        if (this.getItemStorage() == null)
            return ItemStack.EMPTY;
        return this.getItemStorage().extractItem(0, 1, simulate);
	}

	@Nullable
	@Override
	public IItemHandler getItemStorage() {
		return this.presentBlockEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech ? breech.createItemHandler() : null;
	}

}
