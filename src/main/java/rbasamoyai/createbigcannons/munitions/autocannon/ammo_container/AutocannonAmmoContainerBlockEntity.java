package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.remix.CBCHasIItemHandlerBlockEntity;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class AutocannonAmmoContainerBlockEntity extends BlockEntity implements IAutocannonAmmoContainerContainer,
    MenuProvider, Nameable, CBCHasIItemHandlerBlockEntity, PartialSafeNBT {

	private int currentIndex = 0;
    private IItemHandler inventory;

	private ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
		@Override
		protected void onOpen(Level level, BlockPos pos, BlockState state) {
			AutocannonAmmoContainerBlockEntity.this.playSound(SoundEvents.IRON_TRAPDOOR_OPEN);
			AutocannonAmmoContainerBlockEntity.this.updateBlockState(state, true);
		}

		@Override
		protected void onClose(Level level, BlockPos pos, BlockState state) {
			AutocannonAmmoContainerBlockEntity.this.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE);
			AutocannonAmmoContainerBlockEntity.this.updateBlockState(state, false);
		}

		@Override
		protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
		}

		@Override
		protected boolean isOwnContainer(Player player) {
			return player.containerMenu instanceof AutocannonAmmoContainerMenu menu && menu.getContainer() == AutocannonAmmoContainerBlockEntity.this;
		}
	};

	public AutocannonAmmoContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public ItemStack getMainAmmoStack() {
		return this.components().getOrDefault(CBCDataComponents.AMMO, ItemContainerContents.EMPTY).copyOne();
	}

	@Override
	public ItemStack getTracerStack() {
        return this.components().getOrDefault(CBCDataComponents.TRACERS, ItemContainerContents.EMPTY).copyOne();
	}

	public int getSpacing() {
		return Math.max(this.components().getOrDefault(CBCDataComponents.TRACER_SPACING, 1), 1);
	}

	public void setMainAmmoDirect(ItemStack stack) {
		if (stack == null)
            stack = ItemStack.EMPTY;
        PatchedDataComponentMap patched = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        patched.set(CBCDataComponents.AMMO, ItemContainerContents.fromItems(Lists.newArrayList(stack)));
        this.applyComponents(this.components(), patched.asPatch());
	}

	public void setTracersDirect(ItemStack stack) {
        if (stack == null)
            stack = ItemStack.EMPTY;
        PatchedDataComponentMap patched = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        patched.set(CBCDataComponents.TRACERS, ItemContainerContents.fromItems(Lists.newArrayList(stack)));
        this.applyComponents(this.components(), patched.asPatch());
	}

	public void setSpacing(int spacing) {
        PatchedDataComponentMap patched = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        patched.set(CBCDataComponents.TRACER_SPACING, Mth.clamp(spacing, 1, 6));
        this.applyComponents(this.components(), patched.asPatch());
	}

	public boolean canDropInCreative() {
		return !this.getMainAmmoStack().isEmpty() || !this.getTracerStack().isEmpty();
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}


	@Nullable
	@Override
	public Component getCustomName() {
		return this.components().getOrDefault(DataComponents.CUSTOM_NAME, null);
	}

	public void setCustomName(@Nullable Component name) {
        PatchedDataComponentMap patched = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        if (name == null) {
            patched.remove(DataComponents.CUSTOM_NAME);
        } else {
            patched.set(DataComponents.CUSTOM_NAME, name);
        }
		this.applyComponents(this.components(), patched.asPatch());
	}

    protected Component getDefaultName() {
        return Component.translatable(this.isCreativeContainer()
            ? CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.get().getDescriptionId()
            : CBCBlocks.AUTOCANNON_AMMO_CONTAINER.get().getDescriptionId());
    }

	@Override
	public Component getName() {
        Component custom = this.getCustomName();
		return custom == null ? this.getDefaultName() : custom;
	}

	@Override
	public Component getDisplayName() {
		return this.getName();
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return AutocannonAmmoContainerMenu.getServerMenuForBlockEntity(i, inventory, this, this.isCreativeContainer());
	}

	public boolean isCreativeContainer() {
		return CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.has(this.getBlockState());
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack ammo = this.getItem(slot);
		if (ammo.isEmpty()) return ItemStack.EMPTY;
		ItemStack split = ammo.split(amount);
		this.setItem(slot, ammo);
		this.setChanged();
		return split;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack ret = this.getItem(slot);
		if (slot == AMMO_SLOT) {
			this.setMainAmmoDirect(ItemStack.EMPTY);
		} else if (slot == TRACER_SLOT) {
			this.setTracersDirect(ItemStack.EMPTY);
		}
		return ret;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == AMMO_SLOT) {
			this.setMainAmmoDirect(stack);
		} else if (slot == TRACER_SLOT) {
			this.setTracersDirect(stack);
		}
		this.setChanged();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (this.level != null && this.openersCounter.getOpenerCount() > 0) {
			BlockState state = this.getBlockState();
			this.level.setBlock(this.getBlockPos(), state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE,
				AutocannonAmmoContainerBlock.State.getFromFilled(this.getTotalCount() > 0)), 3);
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return player.canInteractWithBlock(this.getBlockPos(), 4);
	}

	@Override
	public void clearContent() {
		this.setMainAmmoDirect(ItemStack.EMPTY);
        this.setTracersDirect(ItemStack.EMPTY);
	}

	@Override
	public void startOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public void stopOpen(Player player) {
		if (!this.remove && !player.isSpectator()) {
			this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void recheckOpen() {
		if (!this.remove) {
			this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	void updateBlockState(BlockState state, boolean open) {
		AutocannonAmmoContainerBlock.State containerState = open ? AutocannonAmmoContainerBlock.State.getFromFilled(this.getTotalCount() > 0)
			: AutocannonAmmoContainerBlock.State.CLOSED;
		this.level.setBlock(this.getBlockPos(), state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE, containerState), 3);
	}

	void playSound(SoundEvent sound) {
		double x = (double) this.worldPosition.getX() + 0.5d;
		double y = (double) this.worldPosition.getY() + 0.5d;
		double z = (double) this.worldPosition.getZ() + 0.5d;
		this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
	}

    @Override
    public IItemHandler getItemHandler(Direction side) {
        return this.inventory == null ? this.inventory = new AutocannonAmmoContainerInterface(this) : this.inventory;
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        PatchedDataComponentMap copy = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        copy.set(CBCDataComponents.TRACER_SPACING, this.getSpacing());
        if (this.components().has(DataComponents.CUSTOM_NAME))
            copy.set(DataComponents.CUSTOM_NAME, this.components().get(DataComponents.CUSTOM_NAME));
        this.saveAdditional(tag, registries);
        CBCUtils.saveComponentsToStructureTag(tag, copy, registries);
    }

}
