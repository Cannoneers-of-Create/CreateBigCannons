package rbasamoyai.createbigcannons.base;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class ItemStackServerData implements ContainerData {
	private final ItemStack stack;
	private final String tag;

	public ItemStackServerData(ItemStack stack, String tag) {
		this.stack = stack;
		this.tag = tag;
	}

	@Override
	public int get(int index) {
		return index == 0 ? this.stack.get(DataComponents.CUSTOM_DATA).copyTag().getInt(this.tag) : 1; //todo: make this use DataComponents properly
	}

	@Override
	public void set(int index, int value) {
		if (index == 0) this.stack.get(DataComponents.CUSTOM_DATA).copyTag().putInt(this.tag, value);
	}

	@Override
	public int getCount() {
		return 1;
	}
}
