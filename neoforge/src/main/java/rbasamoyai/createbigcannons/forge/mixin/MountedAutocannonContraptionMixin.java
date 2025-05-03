package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.forge.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.forge.mixin_interface.GetItemStorage;

@Mixin(MountedAutocannonContraption.class)
public abstract class MountedAutocannonContraptionMixin extends AbstractMountedCannonContraption implements ItemCannon, GetItemStorage {

	@Override
	public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
		return this.getItemStorage().map(h -> h.insertItem(1, stack, simulate)).orElse(stack);
	}

	@Override
	public ItemStack extractItemFromCannon(boolean simulate) {
		return this.getItemStorage().map(h -> h.extractItem(0, 1, simulate)).orElse(ItemStack.EMPTY);
	}

	@Nonnull
	@Override
	public LazyOptional<IItemHandler> getItemStorage() {
		return this.presentBlockEntities.get(this.startPos) instanceof AutocannonBreechBlockEntity breech
			? LazyOptional.of(breech::createItemHandler)
			: LazyOptional.empty();
	}

}
