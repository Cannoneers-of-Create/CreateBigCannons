package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.ItemStackServerData;
import rbasamoyai.createbigcannons.base.SimpleValueContainer;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

public class AutocannonAmmoContainerMenu extends AbstractContainerMenu implements SimpleValueContainer {

	private static final ResourceLocation TRACER_SLOT = CreateBigCannons.resource("item/tracer_slot");

	public static AutocannonAmmoContainerMenu getServerMenuForItemStack(int id, Inventory playerInv, ItemStack stack, boolean isCreative) {
		IAutocannonAmmoContainerContainer ct = new AutocannonAmmoContainerItemContainer(stack);
		return new AutocannonAmmoContainerMenu(CBCMenuTypes.AUTOCANNON_AMMO_CONTAINER.get(), id, playerInv, ct, new ItemStackServerData(stack, "TracerSpacing"), isCreative, true);
	}

	public static AutocannonAmmoContainerMenu getServerMenuForBlockEntity(int id, Inventory playerInv, AutocannonAmmoContainerBlockEntity be, boolean isCreative) {
		return new AutocannonAmmoContainerMenu(CBCMenuTypes.AUTOCANNON_AMMO_CONTAINER.get(), id, playerInv, be, new AutocannonAmmoContainerServerData(be), isCreative, false);
	}

	public static AutocannonAmmoContainerMenu getClientMenu(MenuType<AutocannonAmmoContainerMenu> type, int id, Inventory playerInv, FriendlyByteBuf buf) {
		boolean isCreative = buf.readBoolean();
		ContainerData data = new SimpleContainerData(1);
		data.set(0, buf.readVarInt());
		boolean isBlock = buf.readBoolean();
		IAutocannonAmmoContainerContainer ct;
		if (isBlock) {
			BlockPos pos = buf.readBlockPos();
			BlockEntity be = playerInv.player.level.getBlockEntity(pos);
			ct = new AutocannonAmmoContainerBlockEntityContainerWrapper(be instanceof AutocannonAmmoContainerBlockEntity abe ? abe : null, pos);
		} else {
			ct = new AutocannonAmmoContainerItemContainer(buf.readItem());
		}
		return new AutocannonAmmoContainerMenu(type, id, playerInv, ct, data, isCreative, !isBlock);
	}

	private final boolean isCreative;
	private final IAutocannonAmmoContainerContainer container;
	private final ContainerData data;
	private final Inventory playerInv;
	private final boolean isItem;

	protected AutocannonAmmoContainerMenu(MenuType<? extends AutocannonAmmoContainerMenu> type, int id, Inventory playerInv,
										  IAutocannonAmmoContainerContainer ct, ContainerData data, boolean isCreative, boolean isItem) {
		super(type, id);

		this.addSlot(new AutocannonAmmoContainerMenuSlot(ct, IAutocannonAmmoContainerContainer.AMMO_SLOT, 32, 26, isCreative));
		this.addSlot(new AutocannonAmmoContainerMenuSlot(ct, IAutocannonAmmoContainerContainer.TRACER_SLOT, 59, 26, isCreative) {
			@Nullable
			@Override
			public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
				return Pair.of(InventoryMenu.BLOCK_ATLAS, TRACER_SLOT);
			}
		});

		int add = isCreative ? 18 : 8;
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlot(new Slot(playerInv, row * 9 + col + 9, col * 18 + add, row * 18 + 105));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInv, i, i * 18 + add, 163));
		}

		this.addDataSlots(data);
		this.data = data;
		this.container = ct;
		this.playerInv = playerInv;
		this.isCreative = isCreative;
		this.isItem = isItem;

		this.container.startOpen(playerInv.player);
	}

	@Override public boolean stillValid(Player player) { return this.container.stillValid(player); }

	public int getValue() { return this.data.get(0); }
	public boolean isCreativeContainer() { return this.isCreative; };

	@Override public void setValue(int value) { this.data.set(0, value); }

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (index < 2) {
				if (!this.moveItemStackTo(itemStack2, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!(itemStack2.getItem() instanceof AutocannonAmmoItem ammoItem)) return ItemStack.EMPTY;
				AutocannonAmmoType ammoType = ammoItem.getType();
				if (ammoType == AutocannonAmmoType.NONE) return ItemStack.EMPTY;

				AutocannonAmmoType ctType = this.container.getAmmoType();
				int buf = ctType == AutocannonAmmoType.NONE
					? ammoType.getCapacity()
					: Math.max(ctType.getCapacity() - this.container.getTotalCount(), 0);
				if (buf < 1) return ItemStack.EMPTY;

				int insertIndex = ammoItem.isTracer(itemStack2) ? 1 : 0;
				Slot insertSlot = this.slots.get(insertIndex);
				if (insertSlot != null) insertSlot.safeInsert(itemStack2);
			}

			if (itemStack2.isEmpty()) slot.set(ItemStack.EMPTY);
			else slot.setChanged();

			if (itemStack2.getCount() == itemStack.getCount()) return ItemStack.EMPTY;
			slot.onTake(player, itemStack2);
		}
		return itemStack;
	}

	public boolean isFilled() { return this.container.getAmmoType() != AutocannonAmmoType.NONE; }

	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		if (slotId == this.playerInv.selected + 29 && clickType != ClickType.THROW && this.isItem) return;
		super.clicked(slotId, button, clickType, player);
	}

	public IAutocannonAmmoContainerContainer getContainer() { return this.container; }

	@Override
	public void removed(Player player) {
		super.removed(player);
		this.container.stopOpen(player);
	}

}
