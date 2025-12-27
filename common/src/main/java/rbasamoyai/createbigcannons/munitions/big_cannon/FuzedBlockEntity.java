package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzedBlockEntity extends BigCannonProjectileBlockEntity {

	protected ItemStack fuze = ItemStack.EMPTY;

	public FuzedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.fuze.isEmpty()) {
			tag.put("Fuze", this.fuze.save(registries));
		}
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.fuze = ItemStack.parseOptional(registries, tag.getCompound("Fuze"));
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		CreateLang.builder("block")
			.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
			.style(ChatFormatting.YELLOW)
			.forGoggles(tooltip);
		if (!this.fuze.isEmpty() && this.fuze.getItem() instanceof FuzeItem fuzeItem) {
			CreateLang.builder()
				.add(fuzeItem.getDescription().copy())
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip, 1);
			fuzeItem.addExtraInfo(tooltip, isPlayerSneaking, this.fuze);
		} else {
			CreateLang.builder("block")
				.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze.none")
				.style(ChatFormatting.DARK_GRAY)
				.forGoggles(tooltip, 1);
		}
		return true;
	}

	@Override
	public int getContainerSize() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && this.fuze.isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return slot == 1 ? this.fuze : super.getItem(slot);
	}

	public ItemStack getFuze() {
		return this.fuze;
	}

	public boolean hasFuze() {
		return !this.fuze.isEmpty();
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (slot == 1 && amount > 0)
			return this.getItem(slot).split(amount);
		return super.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot == 1) {
			if (this.fuze.isEmpty())
				return ItemStack.EMPTY;
			ItemStack result = this.fuze;
			this.fuze = ItemStack.EMPTY;
			return result;
		}
		return super.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == 1) {
			this.fuze = stack;
			if (stack.getCount() > this.getMaxStackSize())
				stack.setCount(this.getMaxStackSize());
			this.setChanged();
			return;
		}
		super.setItem(slot, stack);
	}

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index == 1) {
            return stack.getItem() instanceof FuzeItem && this.fuze.isEmpty();
        }
        return super.canPlaceItem(index, stack);
    }

    @Override
	public void clearContent() {
		this.fuze = ItemStack.EMPTY;
		super.clearContent();
	}

}
