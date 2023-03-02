package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.CBCMenuTypes;

public class TimedFuzeContainer extends AbstractContainerMenu {

	private final ContainerData data;
	
	public static TimedFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new TimedFuzeContainer(CBCMenuTypes.SET_TIMED_FUZE.get(), id, new ServerData(stack), ItemStack.EMPTY);
	}
	
	public static TimedFuzeContainer getClientMenu(MenuType<TimedFuzeContainer> type, int id, Inventory playerInv, FriendlyByteBuf buf) {
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		return new TimedFuzeContainer(type, id, data, buf.readItem());
	}
	
	private final ItemStack stackToRender;
	
	protected TimedFuzeContainer(MenuType<? extends TimedFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId);
		this.data = data;
		this.stackToRender = stackToRender;
		this.addDataSlots(this.data);
	}
	
	public void setTime(int time) { this.data.set(0, time); }
	public int getTime() { return this.data.get(0); }
	public ItemStack getStackToRender() { return this.stackToRender; }

	@Override public boolean stillValid(Player player) { return true; }

	private static class ServerData implements ContainerData {
		private final ItemStack stack;
		
		public ServerData(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public int get(int index) {
			return index == 0 ? this.stack.getOrCreateTag().getInt("FuzeTimer") : 0;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) this.stack.getOrCreateTag().putInt("FuzeTimer", value);
		}

		@Override public int getCount() { return 1; }
	}
	
}
