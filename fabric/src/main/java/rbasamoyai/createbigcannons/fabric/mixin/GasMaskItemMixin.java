package rbasamoyai.createbigcannons.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;

@Mixin(GasMaskItem.class)
public class GasMaskItemMixin {

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
	private static Item.Properties createbigcannons$init(Item.Properties properties) {
		if (properties instanceof FabricItemSettings settings)
			settings.equipmentSlot(GasMaskItemMixin::getSlot);
		return properties;
	}

	private static EquipmentSlot getSlot(ItemStack stack) { return EquipmentSlot.HEAD; }

}
