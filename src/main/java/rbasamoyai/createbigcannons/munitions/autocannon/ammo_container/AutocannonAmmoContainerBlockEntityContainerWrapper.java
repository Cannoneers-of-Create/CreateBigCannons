package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AutocannonAmmoContainerBlockEntityContainerWrapper implements IAutocannonAmmoContainerContainer {

	@Nullable private final AutocannonAmmoContainerBlockEntity be;
	private final BlockPos pos;

	public AutocannonAmmoContainerBlockEntityContainerWrapper(@Nullable AutocannonAmmoContainerBlockEntity be, BlockPos pos) {
		this.be = be;
		this.pos = pos;
	}

	@Override public ItemStack getMainAmmoStack() { return this.be == null ? ItemStack.EMPTY : this.be.getMainAmmoStack(); }
	@Override public ItemStack getTracerStack() { return this.be == null ? ItemStack.EMPTY : this.be.getTracerStack(); }
	@Override public ItemStack removeItem(int slot, int amount) { return this.be == null ? ItemStack.EMPTY : this.be.removeItem(slot, amount); }
	@Override public ItemStack removeItemNoUpdate(int slot) { return this.be == null ? ItemStack.EMPTY : this.be.removeItemNoUpdate(slot); }
	@Override public void setItem(int slot, ItemStack stack) { if (this.be != null) this.be.setItem(slot, stack); }
	@Override public void setChanged() { if (this.be != null) this.be.setChanged(); }
	@Override public void clearContent() { if (this.be != null) this.be.clearContent(); }

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return this.be != null && this.be.canPlaceItem(index, stack);
	}

	@Override
	public boolean stillValid(Player player) {
		return this.pos.closerThan(player.blockPosition(), 4);
	}

}
