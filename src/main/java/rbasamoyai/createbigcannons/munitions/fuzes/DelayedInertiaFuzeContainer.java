package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.base.ItemStackServerData;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;

public class DelayedInertiaFuzeContainer extends AbstractFuzeContainer {

	public static DelayedInertiaFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new DelayedInertiaFuzeContainer(CBCMenuTypes.SET_DELAYED_INERTIA_FUZE.get(), id, new ItemStackServerData(stack, CBCDataComponents.FUZE_TIMER), ItemStack.EMPTY);
	}

    public static DelayedInertiaFuzeContainer getClientMenu(MenuType<DelayedInertiaFuzeContainer> type, int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
        ContainerData data = new SimpleContainerData(1);
        data.set(0, buf.readVarInt());
        return new DelayedInertiaFuzeContainer(type, id, data, ItemStack.STREAM_CODEC.decode(buf));
    }

	protected DelayedInertiaFuzeContainer(MenuType<? extends AbstractFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
		super(type, windowId, data, stackToRender);
	}

}
