package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import com.google.common.collect.Lists;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class AutocannonAmmoContainerItemContainer implements IAutocannonAmmoContainerContainer {

	private final ItemStack stack;

	public AutocannonAmmoContainerItemContainer(ItemStack stack) {
		this.stack = stack;
	}

	@Override public ItemStack getMainAmmoStack() { return AutocannonAmmoContainerItem.getMainAmmoStack(this.stack); }
	@Override public ItemStack getTracerStack() { return AutocannonAmmoContainerItem.getTracerAmmoStack(this.stack); }

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (amount <= 0 || slot != 0 && slot != 1) return ItemStack.EMPTY;
		ItemStack ammo = this.getItem(slot);
		if (ammo.isEmpty()) return ItemStack.EMPTY;
		ItemStack split = ammo.split(amount);
		this.stack.set(slot == AMMO_SLOT ? CBCDataComponents.AMMO : CBCDataComponents.TRACERS, ItemContainerContents.fromItems(Lists.newArrayList(ammo)));
		this.setChanged();
		return split;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot != 0 && slot != 1) return ItemStack.EMPTY;
		ItemStack ret = this.getItem(slot);
        this.stack.set(slot == AMMO_SLOT ? CBCDataComponents.AMMO : CBCDataComponents.TRACERS, ItemContainerContents.EMPTY);
		return ret;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot != 0 && slot != 1) return;
        this.stack.set(slot == AMMO_SLOT ? CBCDataComponents.AMMO : CBCDataComponents.TRACERS, ItemContainerContents.fromItems(Lists.newArrayList(stack)));
		this.setChanged();
	}

	@Override
	public void setChanged() {

	}

	@Override public boolean stillValid(Player player) { return true; }

	@Override
	public void clearContent() {
		this.stack.set(CBCDataComponents.AMMO, ItemContainerContents.EMPTY);
		this.stack.set(CBCDataComponents.TRACERS, ItemContainerContents.EMPTY);
	}

	@Override
	public void startOpen(Player player) {
		player.level().playSound(player, player.blockPosition(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 0.5F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public void stopOpen(Player player) {
		player.level().playSound(player, player.blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.5F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
	}

}
