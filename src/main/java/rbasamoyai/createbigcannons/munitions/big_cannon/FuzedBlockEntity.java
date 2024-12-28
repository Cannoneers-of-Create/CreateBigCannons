package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.fuze.isEmpty()) {
			tag.put("Fuze", this.fuze.save(new CompoundTag()));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		Lang.builder("block")
			.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
			.style(ChatFormatting.YELLOW)
			.forGoggles(tooltip);
		if (!this.fuze.isEmpty() && this.fuze.getItem() instanceof FuzeItem fuzeItem) {
			Lang.builder()
				.add(fuzeItem.getDescription().copy())
				.style(ChatFormatting.GREEN)
				.forGoggles(tooltip, 1);
			fuzeItem.addExtraInfo(tooltip, isPlayerSneaking, this.fuze);
		} else {
			Lang.builder("block")
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

	public ItemStack getFuze() { return this.fuze; }

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
	public void clearContent() {
		this.fuze = ItemStack.EMPTY;
		super.clearContent();
	}

}
