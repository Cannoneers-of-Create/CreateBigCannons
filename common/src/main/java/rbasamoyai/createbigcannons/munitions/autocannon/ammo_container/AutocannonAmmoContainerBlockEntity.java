package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class AutocannonAmmoContainerBlockEntity extends BlockEntity implements IAutocannonAmmoContainerContainer, MenuProvider, Nameable {

	private ItemStack ammo = ItemStack.EMPTY;
	private ItemStack tracers = ItemStack.EMPTY;
	private int spacing = 1;
	private int currentIndex = 0;
	private Component name;

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

	@Override public ItemStack getMainAmmoStack() { return this.ammo == null ? ItemStack.EMPTY : this.ammo; }
	@Override public ItemStack getTracerStack() { return this.tracers == null ? ItemStack.EMPTY : this.tracers; }

	public int getSpacing() { return this.spacing; }

	public void setMainAmmoDirect(ItemStack stack) { this.ammo = stack == null ? ItemStack.EMPTY : stack; }
	public void setTracersDirect(ItemStack stack) { this.tracers = stack == null ? ItemStack.EMPTY : stack; }
	public void setSpacing(int spacing) { this.spacing = Mth.clamp(spacing, 1, 6); }

	public boolean canDropInCreative() { return !this.getMainAmmoStack().isEmpty() || !this.getTracerStack().isEmpty(); }

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.ammo != null && !this.ammo.isEmpty()) tag.put("Ammo", this.ammo.save(new CompoundTag()));
		if (this.tracers != null && !this.tracers.isEmpty()) tag.put("Tracers", this.tracers.save(new CompoundTag()));
		if (this.name != null) tag.putString("CustomName", Component.Serializer.toJson(this.name));
		if (this.isCreativeContainer()) tag.putInt("CurrentIndex", this.currentIndex);
		tag.putInt("TracerSpacing", this.spacing);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.ammo = tag.contains("Ammo") ? ItemStack.of(tag.getCompound("Ammo")) : ItemStack.EMPTY;
		this.tracers = tag.contains("Tracers") ? ItemStack.of(tag.getCompound("Tracers")) : ItemStack.EMPTY;
		this.spacing = tag.contains("TracerSpacing") ? Mth.clamp(tag.getInt("TracerSpacing"), 1, 6) : 1;
		this.name = tag.contains("CustomName", Tag.TAG_STRING) ? Component.Serializer.fromJson(tag.getString("CustomName")) : null;
		this.currentIndex = tag.contains("CurrentIndex", Tag.TAG_INT) ? tag.getInt("CurrentIndex") : 0;
	}

	@Override
	public void saveToItem(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (this.ammo != null && !this.ammo.isEmpty()) tag.put("Ammo", this.ammo.save(new CompoundTag()));
		if (this.tracers != null && !this.tracers.isEmpty()) tag.put("Tracers", this.tracers.save(new CompoundTag()));
		if (this.isCreativeContainer()) tag.putInt("CurrentIndex", this.currentIndex);
		tag.putInt("TracerSpacing", this.spacing);
	}

	@Override public CompoundTag getUpdateTag() { return this.saveWithFullMetadata(); }

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}


	@Nullable
	@Override
	public Component getCustomName() { return this.name; }

	public void setCustomName(Component name) { this.name = name; }

	protected Component getDefaultName() {
		return new TranslatableComponent(CBCBlocks.AUTOCANNON_AMMO_CONTAINER.get().getDescriptionId());
	}

	@Override public Component getName() { return this.name == null ? this.getDefaultName() : this.name; }
	@Override public Component getDisplayName() { return this.getName(); }

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return AutocannonAmmoContainerMenu.getServerMenuForBlockEntity(i, inventory, this, this.isCreativeContainer());
	}

	public boolean isCreativeContainer() { return CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.has(this.getBlockState()); }

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
			this.ammo = ItemStack.EMPTY;
		} else if (slot == TRACER_SLOT) {
			this.tracers = ItemStack.EMPTY;
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
		return this.getBlockPos().closerThan(player.blockPosition(), 4);
	}

	@Override
	public void clearContent() {
		this.ammo = ItemStack.EMPTY;
		this.tracers = ItemStack.EMPTY;
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

}
