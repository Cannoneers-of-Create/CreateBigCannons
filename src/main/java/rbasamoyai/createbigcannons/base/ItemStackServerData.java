package rbasamoyai.createbigcannons.base;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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
		return index == 0 ? ((CompoundTag) this.stack.saveOptional(Minecraft.getInstance().level.registryAccess())).getInt(this.tag) : 1;
	}

	@Override
	public void set(int index, int value) {
		if (index == 0) ((CompoundTag) this.stack.saveOptional(Minecraft.getInstance().level.registryAccess())).putInt(this.tag, value);
	}

	@Override
	public int getCount() {
		return 1;
	}
}
