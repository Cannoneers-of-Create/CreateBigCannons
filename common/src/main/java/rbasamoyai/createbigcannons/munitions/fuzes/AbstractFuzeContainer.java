package rbasamoyai.createbigcannons.munitions.fuzes;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.base.SimpleValueContainer;

public abstract class AbstractFuzeContainer extends AbstractContainerMenu implements SimpleValueContainer {

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


}
