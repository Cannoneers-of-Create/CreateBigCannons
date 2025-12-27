package rbasamoyai.createbigcannons.base;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class ItemStackServerData implements ContainerData {
	private final ItemStack stack;
	private final DataComponentType<Integer> component;

	public ItemStackServerData(ItemStack stack, DataComponentType<Integer> component) {
		this.stack = stack;
		this.component = component;
	}

	@Override
	public int get(int index) {
		return index == 0 && this.stack.has(this.component) ? this.stack.get(this.component) : 1;
	}

	@Override
	public void set(int index, int value) {
		if (index == 0) this.stack.set(this.component, value);
	}

	@Override
	public int getCount() {
		return 1;
	}
}
