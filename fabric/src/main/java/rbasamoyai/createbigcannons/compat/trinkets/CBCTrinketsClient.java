package rbasamoyai.createbigcannons.compat.trinkets;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCTrinketsClient {

	public static void initClient() {
		TrinketRendererRegistry.registerRenderer(CBCItems.GAS_MASK.get(), new GasMaskTrinketRenderer());
	}

}
