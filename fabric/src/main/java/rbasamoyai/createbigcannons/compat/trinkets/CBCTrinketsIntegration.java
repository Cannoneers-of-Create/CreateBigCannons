package rbasamoyai.createbigcannons.compat.trinkets;

import java.util.Optional;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.index.CBCItems;

// Adapted from Create's Trinkets class --ritchie
public class CBCTrinketsIntegration {

	public static void init() {
		GasMaskItem.addIsWearingPredicate(player -> {
			Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
            return optional.isPresent() && optional.get().isEquipped(CBCTrinketsIntegration::isGasMask);
        });
		GasMaskItem.addOverlayDisplayPredicate(player -> {
			Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);
            return optional.isPresent() && optional.get().isEquipped(CBCTrinketsIntegration::isCBCGasMask);
        });
	}

	private static boolean isGasMask(ItemStack itemStack) { return itemStack.is(CBCItemTags.GAS_MASKS); }

	private static boolean isCBCGasMask(ItemStack itemStack) { return CBCItems.GAS_MASK.isIn(itemStack); }

}
