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

public class ProximityFuzeContainer extends AbstractFuzeContainer {

	public static ProximityFuzeContainer getServerMenu(int id, Inventory playerInv, ItemStack stack) {
		return new ProximityFuzeContainer(CBCMenuTypes.SET_PROXIMITY_FUZE.get(), id, new ItemStackServerData(stack, CBCDataComponents.DETONATION_DISTANCE), ItemStack.EMPTY);
	}

	public static ProximityFuzeContainer getClientMenu(MenuType<ProximityFuzeContainer> type, int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		return new ProximityFuzeContainer(type, id, data, ItemStack.STREAM_CODEC.decode(buf));
	}

    protected ProximityFuzeContainer(MenuType<? extends ProximityFuzeContainer> type, int windowId, ContainerData data, ItemStack stackToRender) {
        super(type, windowId, data, stackToRender);
    }
}
