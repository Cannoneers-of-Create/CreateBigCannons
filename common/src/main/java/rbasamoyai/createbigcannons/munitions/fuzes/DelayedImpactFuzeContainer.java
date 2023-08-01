package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.base.ItemStackServerData;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;

public class DelayedImpactFuzeContainer extends AbstractFuzeContainer {

	public static DelayedImpactFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new DelayedImpactFuzeContainer(CBCMenuTypes.SET_DELAYED_IMPACT_FUZE.get(), id, new ItemStackServerData(stack, "FuzeTimer"), ItemStack.EMPTY);
	}

	public static DelayedImpactFuzeContainer getClientMenu(MenuType<DelayedImpactFuzeContainer> type, int id, Inventory playerInv, FriendlyByteBuf buf) {
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		return new DelayedImpactFuzeContainer(type, id, data, buf.readItem());
	}

	protected DelayedImpactFuzeContainer(MenuType<? extends AbstractFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId, data, stackToRender);
	}

}
