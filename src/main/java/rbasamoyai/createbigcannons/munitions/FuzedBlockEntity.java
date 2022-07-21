package rbasamoyai.createbigcannons.munitions;

import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzedBlockEntity extends SyncedTileEntity {

	private final ItemStackHandler fuzeHandler = new ItemStackHandler(1) {
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (!(stack.getItem() instanceof FuzeItem)) return stack;
			return super.insertItem(slot, stack, simulate);
		}
		
		@Override public int getSlotLimit(int slot) { return 1; }
	};
	
	private final LazyOptional<IItemHandler> fuzeOptional = LazyOptional.of(() -> this.fuzeHandler);
	
	public FuzedBlockEntity(BlockEntityType<? extends FuzedBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == this.getBlockState().getValue(BlockStateProperties.FACING)) {
			return this.fuzeOptional.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		this.fuzeOptional.invalidate();
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.fuzeHandler.getStackInSlot(0).isEmpty()) {
			tag.put("Fuze", this.fuzeHandler.getStackInSlot(0).serializeNBT());
		}
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
			this.fuzeHandler.setStackInSlot(0, ItemStack.of(tag.getCompound("Fuze")));
		} else {
			this.fuzeHandler.setStackInSlot(0, ItemStack.EMPTY);
		}
	}
	
}
