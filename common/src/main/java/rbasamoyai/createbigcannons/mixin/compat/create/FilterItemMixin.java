package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.simibubi.create.content.logistics.filter.FilterItem;

import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

@Mixin(FilterItem.class)
public class FilterItemMixin {

	@Inject(method = "testDirect", at = @At("HEAD"), cancellable = true)
	private static void createbigcannons$testDirect(ItemStack filter, ItemStack stack, boolean matchNBT, CallbackInfoReturnable<Boolean> cir) {
		if (matchNBT) return;
		if (CBCBlocks.BIG_CARTRIDGE.is(filter.getItem()) && BigCartridgeBlockItem.getPower(filter) == 0) {
			cir.setReturnValue(CBCBlocks.BIG_CARTRIDGE.is(stack.getItem()) && BigCartridgeBlockItem.getPower(stack) == 0);
			return;
		}
		if (filter.getItem() instanceof AutocannonAmmoContainerItem && AutocannonAmmoContainerItem.getTotalAmmoCount(filter) == 0) {
			cir.setReturnValue(stack.getItem() instanceof AutocannonAmmoContainerItem && AutocannonAmmoContainerItem.getTotalAmmoCount(stack) == 0);
			return;
		}
	}

}
