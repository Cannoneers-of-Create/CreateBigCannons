package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;

@Mixin(GasMaskItem.class)
public class GasMaskItemMixin extends Item {

	GasMaskItemMixin(Properties properties) { super(properties); }

	@Nullable
	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

}
