package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;

public class BigCannonProjectileBlockEntity extends SyncedBlockEntity implements IHaveGoggleInformation, Container {

	public BigCannonProjectileBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

    @Override
    public CompoundTag writeClient(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeClient(tag, registries);
        ItemStack tracer = this.getTracer();
        if (!tracer.isEmpty())
            tag.put("Tracer", tracer.save(registries));
        return tag;
    }

    @Override
    public void readClient(CompoundTag tag, HolderLookup.Provider registries) {
        super.readClient(tag, registries);
        this.setTracer(ItemStack.parseOptional(registries, tag.getCompound("Tracer")));
    }

    @Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.getTracer().isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return slot == 0 ? this.getTracer() : ItemStack.EMPTY;
	}

	public ItemStack getTracer() {
        ItemContainerContents contents = this.components().getOrDefault(CBCDataComponents.TRACER, ItemContainerContents.EMPTY);
        return contents.getSlots() > 0 ? contents.getStackInSlot(0) : ItemStack.EMPTY;
	}

    public void setTracer(ItemStack itemStack) {
        PatchedDataComponentMap components = new PatchedDataComponentMap(this.components());
        if (itemStack.isEmpty()) {
            components.remove(CBCDataComponents.TRACER);
        } else {
            components.set(CBCDataComponents.TRACER, ItemContainerContents.fromItems(List.of(itemStack)));
        }
        this.setComponents(components);
    }

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (this.isEmpty() || slot != 0 || amount < 1)
			return ItemStack.EMPTY;
        ItemStack originalCopy = this.getTracer();
        ItemStack result = originalCopy.split(amount);
        this.setTracer(originalCopy);
		return result;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (this.isEmpty() || slot != 0)
			return ItemStack.EMPTY;
		ItemStack result = this.getTracer();
		this.setTracer(ItemStack.EMPTY);
		return result;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot != 0)
			return;
		this.setTracer(stack);
		if (stack.getCount() > this.getMaxStackSize())
			stack.setCount(this.getMaxStackSize());
		this.setChanged();
	}

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index == 0 && CBCItems.TRACER_TIP.isIn(stack) && this.getTracer().isEmpty();
    }

    @Override
	public boolean stillValid(Player player) {
		return false;
	}

	@Override
	public void clearContent() {
		this.setTracer(ItemStack.EMPTY);
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!this.getTracer().isEmpty())
			CreateLang.builder("tooltip")
			.translate("createbigcannons.tracer")
			.forGoggles(tooltip);
		return true;
	}
}
