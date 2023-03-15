package rbasamoyai.createbigcannons.munitions.big_cannon;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.List;

public class FuzedBlockEntity extends SyncedTileEntity implements IHaveGoggleInformation, Container {

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

	@Override public int getContainerSize() { return 1; }
	@Override public boolean isEmpty() { return this.fuze.isEmpty(); }
	@Override public ItemStack getItem(int slot) { return slot == 0 ? this.fuze : ItemStack.EMPTY; }

	public boolean hasFuze() { return !this.fuze.isEmpty(); }

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (this.isEmpty() || slot != 0 || amount < 1) return ItemStack.EMPTY;
		return this.getItem(slot).split(amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (this.isEmpty() || slot != 0) return ItemStack.EMPTY;
		ItemStack result = this.fuze;
		this.fuze = ItemStack.EMPTY;
		return result;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot != 0) return;
		this.fuze = stack;
		if (stack.getCount() > this.getMaxStackSize()) stack.setCount(this.getMaxStackSize());
		this.setChanged();
	}

	@Override public int getMaxStackSize() { return 1; }

	@Override public boolean stillValid(Player player) { return false; }
	@Override public void clearContent() { this.fuze = ItemStack.EMPTY; }

}
