package rbasamoyai.createbigcannons.compat.computercraft.details;

import com.simibubi.create.foundation.utility.Lang;

import dan200.computercraft.api.detail.BasicItemDetailProvider;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.HashMap;
import java.util.Map;

public class FuzedBlockItemDetails extends BasicItemDetailProvider<FuzedProjectileBlockItem> {
	public FuzedBlockItemDetails() {
		super(FuzedProjectileBlockItem.class);
	}

	@Override
	public void provideDetails(Map<? super String, Object> data, ItemStack stack, FuzedProjectileBlockItem item) {
		CompoundTag tag = stack.getOrCreateTag();
		ItemStack fuze = ItemStack.of(tag.getCompound("BlockEntityTag").getCompound("Fuze"));
		if (!fuze.isEmpty()) {
			Map<? super String, Object> fuzeData = new HashMap<>();
			if (fuze.getItem() instanceof FuzeItem fuzeItem) {
				int time = fuze.getOrCreateTag().getInt("FuzeTimer");
				int detonationDistance = fuze.getOrCreateTag().getInt("DetonationDistance");
				fuzeData.put("name", fuze.getDisplayName().getString());
				fuzeData.put("fuzeTimer", time);
				fuzeData.put("detonationDistance", detonationDistance);
			}
			data.put("fuze", fuzeData);
		}
		data.put("tracerName", tag.getCompound("BlockEntityTag").getCompound("Tracer").get("id").getAsString());
	}
}
