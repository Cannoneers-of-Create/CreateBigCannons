package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractFuzeContainer extends AbstractContainerMenu {

	protected final ContainerData data;

	private final ItemStack stackToRender;

	protected AbstractFuzeContainer(MenuType<? extends AbstractFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId);
		this.data = data;
		this.stackToRender = stackToRender;
		this.addDataSlots(this.data);
	}

	public void setValue(int distance) { this.data.set(0, distance); }
	public int getValue() { return this.data.get(0); }
	public ItemStack getStackToRender() { return this.stackToRender; }

	@Override public boolean stillValid(Player player) { return true; }

	protected static class ServerData implements ContainerData {
		private final ItemStack stack;
		private final String tag;

		public ServerData(ItemStack stack, String tag) {
			this.stack = stack;
			this.tag = tag;
		}

		@Override
		public int get(int index) {
			return index == 0 ? this.stack.getOrCreateTag().getInt(this.tag) : 1;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) this.stack.getOrCreateTag().putInt(this.tag, value);
		}

		@Override public int getCount() { return 1; }
	}


}
