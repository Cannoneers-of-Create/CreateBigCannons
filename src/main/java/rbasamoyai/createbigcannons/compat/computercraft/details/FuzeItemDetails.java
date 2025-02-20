package rbasamoyai.createbigcannons.compat.computercraft.details;

import dan200.computercraft.api.detail.BasicItemDetailProvider;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.Map;

public class FuzeItemDetails extends BasicItemDetailProvider<FuzeItem> {
	public FuzeItemDetails() {
		super(FuzeItem.class);
	}

	@Override
	public void provideDetails(Map<? super String, Object> data, ItemStack stack, FuzeItem item) {
		int time = stack.getOrCreateTag().getInt("FuzeTimer");
		int detonationDistance = stack.getOrCreateTag().getInt("DetonationDistance");

		data.put("fuzeTimer", time);
		data.put("detonationDistance", detonationDistance);
	}
}
