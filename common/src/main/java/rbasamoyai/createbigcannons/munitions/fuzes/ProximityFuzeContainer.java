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

public class ProximityFuzeContainer extends AbstractContainerMenu {

private final ContainerData data;
	
	public static ProximityFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new ProximityFuzeContainer(CBCMenuTypes.SET_PROXIMITY_FUZE.get(), id, new ServerData(stack), ItemStack.EMPTY);
	}
	
	public static ProximityFuzeContainer getClientMenu(MenuType<ProximityFuzeContainer> type, int id, Inventory playerInv, FriendlyByteBuf buf) {
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		return new ProximityFuzeContainer(type, id, data, buf.readItem());
	}
	
	private final ItemStack stackToRender;
	
	protected ProximityFuzeContainer(MenuType<? extends ProximityFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId);
		this.data = data;
		this.stackToRender = stackToRender;
		this.addDataSlots(this.data);
	}
	
	public void setDistance(int distance) { this.data.set(0, distance); }
	public int getDistance() { return this.data.get(0); }
	public ItemStack getStackToRender() { return this.stackToRender; }

	@Override public boolean stillValid(Player player) { return true; }

	private static class ServerData implements ContainerData {
		private final ItemStack stack;
		
		public ServerData(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public int get(int index) {
			return index == 0 ? this.stack.getOrCreateTag().getInt("DetonationDistance") : 1;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0) this.stack.getOrCreateTag().putInt("DetonationDistance", value);
		}

		@Override public int getCount() { return 1; }
	}

}
