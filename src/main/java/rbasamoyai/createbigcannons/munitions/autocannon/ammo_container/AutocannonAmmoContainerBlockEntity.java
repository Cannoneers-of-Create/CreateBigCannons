package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

    private IItemHandler inventory;
    private ItemStack ammoStack = ItemStack.EMPTY;
    private ItemStack tracerStack = ItemStack.EMPTY;
    private int tracerSpacing = 1;
    @Nullable private Component name = null;

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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Ammo", this.ammoStack.saveOptional(registries));
        tag.put("Tracers", this.tracerStack.saveOptional(registries));
        tag.putInt("TracerSpacing", this.tracerSpacing);
        if (this.name != null)
            tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.ammoStack = ItemStack.parseOptional(registries, tag.getCompound("Ammo"));
        this.tracerStack = ItemStack.parseOptional(registries, tag.getCompound("Tracers"));
        this.tracerSpacing = Mth.clamp(tag.getInt("TracerSpacing"), 1, 6);
        if (tag.contains("CustomName", Tag.TAG_STRING))
            this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
    }

    @Override public ItemStack getMainAmmoStack() { return this.ammoStack; }

	@Override public ItemStack getTracerStack() { return this.tracerStack; }

	public int getSpacing() { return Math.max(this.tracerSpacing, 1); }

	public void setMainAmmoDirect(ItemStack stack) { this.ammoStack = stack == null ? ItemStack.EMPTY : stack; }

	public void setTracersDirect(ItemStack stack) { this.tracerStack = stack == null ? ItemStack.EMPTY : stack; }

	public void setSpacing(int spacing) { this.tracerSpacing = spacing; }

	public boolean canDropInCreative() {
		return !this.getMainAmmoStack().isEmpty() || !this.getTracerStack().isEmpty();
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        this.ammoStack = componentInput.getOrDefault(CBCDataComponents.AMMO, ItemContainerContents.EMPTY).copyOne();
        this.tracerStack = componentInput.getOrDefault(CBCDataComponents.TRACERS, ItemContainerContents.EMPTY).copyOne();
        this.tracerSpacing = componentInput.getOrDefault(CBCDataComponents.TRACER_SPACING, 1);
        this.name = componentInput.getOrDefault(DataComponents.CUSTOM_NAME, null);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        components.set(CBCDataComponents.AMMO, ItemContainerContents.fromItems(List.of(this.ammoStack)));
        components.set(CBCDataComponents.TRACERS, ItemContainerContents.fromItems(List.of(this.tracerStack)));
        components.set(CBCDataComponents.TRACER_SPACING, this.tracerSpacing);
        components.set(DataComponents.CUSTOM_NAME, this.name);
    }

    @Nullable
	@Override
	public Component getCustomName() { return this.name; }

	public void setCustomName(@Nullable Component name) { this.name = name; }

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
        if (this.isCreativeContainer())
            return null; // Cannot automate creative autocannon ammo container
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

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return !this.isCreativeContainer() && IAutocannonAmmoContainerContainer.super.canPlaceItem(index, stack);
    }

}
