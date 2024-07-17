package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.index.CBCBlocks;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
	private static void createbigcannons$getEquipmentSlotForItem(ItemStack item, CallbackInfoReturnable<EquipmentSlot> cir) {
		if (CBCBlocks.TRAFFIC_CONE.isIn(item))
			cir.setReturnValue(EquipmentSlot.HEAD);
	}

}
