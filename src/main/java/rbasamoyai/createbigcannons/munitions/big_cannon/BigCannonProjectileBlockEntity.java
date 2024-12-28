package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BigCannonProjectileBlockEntity extends SyncedBlockEntity implements IHaveGoggleInformation, Container {

	protected ItemStack tracer = ItemStack.EMPTY;

	public BigCannonProjectileBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.tracer.isEmpty())
			tag.put("Tracer", this.tracer.save(new CompoundTag()));
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.tracer = tag.contains("Tracer", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Tracer")) : ItemStack.EMPTY;
	}

	@Override public int getContainerSize() { return 1; }

	@Override public boolean isEmpty() { return this.tracer.isEmpty(); }

	@Override
	public ItemStack getItem(int slot) {
		return slot == 0 ? this.tracer : ItemStack.EMPTY;
	}

	public ItemStack getTracer() { return this.tracer; }

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (this.isEmpty() || slot != 0 || amount < 1)
			return ItemStack.EMPTY;
		return this.getItem(slot).split(amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (this.isEmpty() || slot != 0)
			return ItemStack.EMPTY;
		ItemStack result = this.tracer;
		this.tracer = ItemStack.EMPTY;
		return result;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot != 0)
			return;
		this.tracer = stack;
		if (stack.getCount() > this.getMaxStackSize())
			stack.setCount(this.getMaxStackSize());
		this.setChanged();
	}

	@Override public boolean stillValid(Player player) { return false; }

	@Override
	public void clearContent() {
		this.tracer = ItemStack.EMPTY;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!this.tracer.isEmpty())
			Lang.builder("tooltip")
				.translate("createbigcannons.tracer")
				.forGoggles(tooltip);
		return true;
	}
}
