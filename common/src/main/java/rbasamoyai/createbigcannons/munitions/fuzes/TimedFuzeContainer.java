package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.base.ItemStackServerData;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;

public class TimedFuzeContainer extends AbstractFuzeContainer {

	public static TimedFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new TimedFuzeContainer(CBCMenuTypes.SET_TIMED_FUZE.get(), id, new ItemStackServerData(stack, "FuzeTimer"), ItemStack.EMPTY);
	}

	public static TimedFuzeContainer getClientMenu(MenuType<TimedFuzeContainer> type, int id, Inventory playerInv, FriendlyByteBuf buf) {
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		return new TimedFuzeContainer(type, id, data, buf.readItem());
	}

	protected TimedFuzeContainer(MenuType<? extends TimedFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId, data, stackToRender);
	}

}
