package rbasamoyai.createbigcannons.base;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

public class ItemStackServerData implements ContainerData {
	private final ItemStack stack;
	private final String[] tags;

	public ItemStackServerData(ItemStack stack, String... tags) {
		this.stack = stack;
		this.tags = tags;
	}

	@Override
	public int get(int index) {
		return this.stack.getOrCreateTag().getInt(this.tags[index]);
	}

	@Override
	public void set(int index, int value) {
		this.stack.getOrCreateTag().putInt(this.tags[index], value);
	}

	@Override
	public int getCount() {
		return tags.length;
	}
}
