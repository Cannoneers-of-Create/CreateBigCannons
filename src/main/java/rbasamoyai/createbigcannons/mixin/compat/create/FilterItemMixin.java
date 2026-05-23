package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.logistics.filter.FilterItem;

import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

@Mixin(FilterItem.class)
public class FilterItemMixin {

	@WrapMethod(method = "testDirect")
	private static boolean createbigcannons$testDirect(ItemStack filter, ItemStack stack, boolean matchNBT, Operation<Boolean> original) {
		if (matchNBT)
            return original.call(filter, stack, true);
		if (CBCBlocks.BIG_CARTRIDGE.is(filter.getItem()) && BigCartridgeBlockItem.getPower(filter) == 0)
            return CBCBlocks.BIG_CARTRIDGE.is(stack.getItem()) && BigCartridgeBlockItem.getPower(stack) == 0;
		if (filter.getItem() instanceof AutocannonAmmoContainerItem && AutocannonAmmoContainerItem.getTotalAmmoCount(filter) == 0)
			return stack.getItem() instanceof AutocannonAmmoContainerItem && AutocannonAmmoContainerItem.getTotalAmmoCount(stack) == 0;
        return original.call(filter, stack, false);
    }

}
